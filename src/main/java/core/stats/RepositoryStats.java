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
import repository.exception.GitRepositoryException;

public class RepositoryStats extends Stats {

  protected String repository;

  public RepositoryStats(Player player, String repository) {
    super(player);
    this.repository = repository;
  }

  public Map<String, Map<String,Double>> derivate() {

    Map<String, Map<String, Double>> result = new HashMap<>();
    Branch[] branches;
    try {
      branches = player.branches(repository);
      for (var branch : branches) {
        var name = branch.getId().getName();
        result.put(name, this.execute(name));
      }
    } catch (GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }

    return result;
  }

  public BranchStats branch(String branchId) {
    return new BranchStats(player, repository, branchId);
  }

  private Map<String, MetricsData> allFilesData(String branch, Commit[] listOfCommits)
      throws GitRepositoryException, GitAPIException {

    return player.metricsFromAllFilesInARangeOfCommits(repository, branch, listOfCommits);
  }

  private Map<String, MetricsData> diffFiles(String branch, Commit[] listOfCommits)
      throws GitRepositoryException, GitAPIException {

    var total = player.metricsFromDiffFilesInARangeOfCommits(repository, branch, listOfCommits);

    total = replaceFirstCommit(total, branch, listOfCommits);

    total = fillNonModifiedFiles(total, listOfCommits);

    return total;
  }

  private Map<String, MetricsData> fillNonModifiedFiles(Map<String, MetricsData> data, Commit[] commits) {

    for (var i = commits.length - 1; i > 0; i--) {
      data.put(commits[i - 1].getId().getName(), createData(commits[i], commits[i - 1], data));
    }

    return data;
  }

  private MetricsData createData(Commit current, Commit next, Map<String, MetricsData> data) {

    var dataCurrent = data.get(current.getId().getName());
    var dataNext = data.get(next.getId().getName());

    var newData = new MetricsData();

    dataCurrent.getFiles().forEach((file,metrics) -> {

      if (!isDeletedFile(next.getDiffFiles(), file)) {
        newData.addFile(file, metrics);
      }
    });

    dataNext.getFiles().forEach((file,metrics) -> {
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

  private Map<String, MetricsData> replaceFirstCommit(Map<String, MetricsData> data, String branch,
      Commit[] listOfCommits) throws GitRepositoryException, GitAPIException {

    var firstCommit = listOfCommits[listOfCommits.length - 1].getId().getName();

    data.put(
      firstCommit, 
      player
        .metricsFromAllFilesInACommit(repository, branch, firstCommit)
    );

    return data;
  }

  public Map<String, MetricsData> data(String branch, Integer from, Integer to, Integer step)
      throws GitRepositoryException, GitAPIException {

    var listOfCommits = extractRangeOfCommits(branch, from, to, step);

    return (step == 1 ? diffFiles(branch, listOfCommits) : allFilesData(branch, listOfCommits));
  }

  private Commit[] extractRangeOfCommits(String branch, Integer from, Integer to, Integer step)
      throws GitAPIException, GitRepositoryException {

    var allCommits = player.commits(repository, branch);
    var listOfCommits = new ArrayList<Commit>();
    for (var i = from; i <= to; i += step) {
      listOfCommits.add(allCommits[i]);
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

  private Map<String,Double> execute(String branch) {
    
    Commit[] commits = null;
    try {
      commits = player.commits(repository, branch);
      return compareInitialAndLastCommit(
        branch,
        commits[commits.length-1].getId().getName(),
        commits[0].getId().getName(),
        commits.length
      );
    } catch (GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Map<String,Double> compareInitialAndLastCommit(
    String branch, String commitBegin, String commitEnd, Integer size)
      throws GitRepositoryException, GitAPIException {

    var metricsStart = getSumOfMetricsOfFiles()
      .apply(
        player
        .metricsFromAllFilesInACommit(repository, branch, commitBegin)
        .getFiles()
      );
    
    var metricsEnd = getSumOfMetricsOfFiles()
      .apply(
        player
        .metricsFromAllFilesInACommit(repository, branch, commitEnd)
        .getFiles()
      );
    
    return derivateMetrics(metricsStart, metricsEnd, size);
  }
}
