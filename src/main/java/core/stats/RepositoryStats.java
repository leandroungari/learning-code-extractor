package core.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.Player;
import core.Stats;
import core.data.Branch;
import core.data.Commit;
import core.data.DiffFile;
import core.data.MetricsData;
import core.features.FeatureType;
import repository.exception.GitRepositoryException;

public class RepositoryStats extends Stats {

  protected String repository;

  public RepositoryStats(Player player, String repository) {
    super(player);
    this.repository = repository;
  }

  public Map<String, Map<String,Double>> derivate(String option) {

    Map<String, Map<String, Double>> result = new HashMap<>();
    Branch[] branches;
    branches = player
      .<Branch[]>get(FeatureType.BRANCHES_OF_REPOSITORY)
      .setParams(repository)
      .action();

    for (var branch : branches) {
      var name = branch.getId().getName();
      result.put(name, execute(name, option));
    }

    return result;
  }

  public BranchStats branch(String branchId) {
    return new BranchStats(player, repository, branchId);
  }

  private Map<String, MetricsData> allFilesData(String branch, Commit[] listOfCommits)
      throws GitRepositoryException, GitAPIException {

    return player
      .metrics(repository)
      .metricsFromAllFilesInARangeOfCommits(branch, listOfCommits);
  }

  private Map<String, MetricsData> diffFiles(
    String branch, 
    Commit[] listOfCommits)
      throws GitRepositoryException, GitAPIException {

    var total = player
      .metrics(repository)
      .metricsFromDiffFilesInARangeOfCommits(branch, listOfCommits);

    total = replaceFirstCommit(total, branch, listOfCommits);
    return fillNonModifiedFiles(total, listOfCommits);
  }

  private Map<String, MetricsData> fillNonModifiedFiles(
    Map<String, MetricsData> data, 
    Commit[] commits) {

    for (var i = 1; i < commits.length; i++) {
      data.put(
        commits[i].getId().getName(), 
        createData(commits[i-1], commits[i], data)
      );
    }

    return data;
  }

  private MetricsData createData(
    Commit current, 
    Commit next, 
    Map<String, MetricsData> data) {

    var dataCurrent = data.get(current.getId().getName());
    var dataNext = data.get(next.getId().getName());

    var newData = new MetricsData();

    dataCurrent
    .getFiles()
    .forEach((file,metrics) -> {
      if (!isDeletedFile(next.getDiffFiles(), file)) {
        newData.addFile(file, metrics);
      }
    });

    dataNext
    .getFiles()
    .forEach((file,metrics) -> {
      newData.addFile(file,metrics);
    });

    return newData;
  }

  private Boolean isDeletedFile(List<DiffFile> listOfFiles, String file) {

    var result = false;
    if (listOfFiles == null)
      return result;

    for(var diff: listOfFiles) {
      if (diff.getOldPath().equals(file) && diff.getType().equals("DELETE")) {
        result = true;
      }
    }
    return result;
  }

  private Map<String, MetricsData> replaceFirstCommit(
    Map<String, MetricsData> data, 
    String branch,
    Commit[] listOfCommits) 
      throws GitRepositoryException, 
             GitAPIException {

    var firstCommit = listOfCommits[0].getId().getName();

    data.put(
      firstCommit, 
      player
      .metrics(repository)
      .metricsFromAllFilesInACommit(branch, firstCommit)
    );

    return data;
  }

  public Map<String, MetricsData> data(
    String branch, 
    Integer from, 
    Integer to, 
    Integer step)
      throws GitRepositoryException, 
             GitAPIException {

    var listOfCommits = rangeOfCommits(branch, from, to, step);
    return (
      step == 1 ? 
      diffFiles(branch, listOfCommits) : 
      allFilesData(branch, listOfCommits)
    );
  }

  public Commit[] rangeOfCommits(
    String branch, 
    Integer from, 
    Integer to, 
    Integer step)
      throws GitAPIException, 
             GitRepositoryException {

    var allCommits = player
      .<Commit[]>get(FeatureType.COMMITS_OF_BRANCH)
      .setParams(repository, branch)
      .action();

    var listOfCommits = new ArrayList<Commit>();
    for (var i = from; i <= to; i += step) {
      listOfCommits.add(allCommits[allCommits.length-i-1]);
    }
    
    return listOfCommits.toArray(new Commit[listOfCommits.size()]);
  }

  public Map<String,Map<String,Double>> extractValuesFromCommits(
    Map<String,MetricsData> data,
    Function<Map<String,Double[]>,Double[]> action) {

    var result = new HashMap<String,Map<String,Double>>();
    data.forEach((commit,files) -> {
      var metrics = action.apply(files.getFiles());
      result.put(commit, extractValuesFromCommit(metrics));
    });
    
    return result;
  }

  private Map<String,Double> extractValuesFromCommit(Double[] data) {
    var result = new HashMap<String,Double>();
    
    result.put("cbo", data[0]);
    result.put("dit", data[1]);
    result.put("nosi", data[2]);
    result.put("rfc", data[3]);
    result.put("wmc", data[4]);

    return result;
  }

  private Map<String,Double> execute(String branch, String type) {
    
    Commit[] commits = null;
    try {
      commits = player
        .<Commit[]>get(FeatureType.COMMITS_OF_BRANCH)
        .setParams(repository, branch)
        .action();

      return compareInitialAndLastCommit(
        branch,
        commits[commits.length-1].getId().getName(),
        commits[0].getId().getName(),
        commits.length,
        type
      );
    } catch (GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Map<String,Double> compareInitialAndLastCommit(
    String branch, 
    String commitBegin, 
    String commitEnd, 
    Integer size,
    String type)
      throws GitRepositoryException, GitAPIException {

    var metricsStart = (
      SUM_OPTION.equals(type) ?
      getSumOfMetricsOfFiles() : 
      getAverageOfMetricsOfFiles() 
    )
    .apply(
      player
      .metrics(repository)
      .metricsFromAllFilesInACommit(branch, commitBegin)
      .getFiles()
    );
    
    var metricsEnd = (
      SUM_OPTION.equals(type) ?
      getSumOfMetricsOfFiles() : 
      getAverageOfMetricsOfFiles() 
    )
    .apply(
      player
      .metrics(repository)
      .metricsFromAllFilesInACommit(branch, commitEnd)
      .getFiles()
    );
    
    return derivateMetrics(metricsStart, metricsEnd, size);
  }
}
