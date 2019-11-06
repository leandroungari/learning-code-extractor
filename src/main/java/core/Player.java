package core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.data.Branch;
import core.data.Commit;
import core.data.CommitInfo;
import core.data.DiffFile;
import core.data.MetricsData;
import core.executor.MetricsExecutor;
import core.executor.RepositoryExecutor;
import core.executor.SettingsExecutor;
import core.resource.Resource;
import core.resource.ResourceType;
import repository.exception.GitRepositoryException;
import util.FileUtil;

public class Player {

  private static RepositoryExecutor repoExec;
  private static MetricsExecutor metricExec;
  private static SettingsExecutor settingsExec;

  public Player() {

    if(!settings().isSet()) {
      settings()
      .start(SettingsExecutor.USER_FOLDER);
    }
    
  }

  public RepositoryExecutor repository() {
    if(repoExec == null) {
      repoExec = new RepositoryExecutor(this);
    }
    return repoExec;
  }

  public MetricsExecutor metrics() {
    if(metricExec == null) {
      metricExec = new MetricsExecutor(this);
    }
    return metricExec;
  }

  public SettingsExecutor settings() {
    if(settingsExec == null) {
      settingsExec = new SettingsExecutor();
    }
    return settingsExec;
  }

  public Branch[] branches(String repositoryName) throws GitAPIException, GitRepositoryException {

    var resource = Resource.create(repositoryName, ResourceType.LIST_OF_BRANCHES);

    if (!resource.exists()) {
      repoExec.branches(repositoryName);
    }

    return resource.read(Branch[].class);
  }

  public Commit[] commits(String repository, String branch) throws GitAPIException, GitRepositoryException {

    var resource = Resource.create(repository, ResourceType.LIST_OF_COMMITS).branch(branch);

    if (!resource.exists()) {
      repoExec.commits(repository, branch);
    }

    return resource.read(Commit[].class);
  }

  public CommitInfo commitInfo(String repository, String branch, String commit)
      throws GitAPIException, GitRepositoryException {

    var resource = Resource
      .create(repository, ResourceType.LIST_OF_COMMITS)
      .branch(branch);

    var listOfCommits = resource.read(Commit[].class);

    for (int i = 0; i < listOfCommits.length; i++) {
      if (commit.equals(listOfCommits[i].getId().getName())) {

        if (listOfCommits[i].getInfo() == null) {
          repoExec.commitInfo(repository, branch, commit);
        }

        return listOfCommits[i].getInfo();
      }
    }

    return null;
  }

  public List<DiffFile> diffFilesFromCommit(String repository, String branch, String commit)
      throws GitRepositoryException, GitAPIException {

    var resource = Resource
      .create(repository, ResourceType.DIFF_FILES)
      .branch(branch);

    if (!resource.exists()) {
      repoExec.commits(repository, branch);
    }

    repoExec.diffFiles(repository, branch, commit);
    var listOfCommits = resource.read(Commit[].class);
    Commit commitItem = null;
    for (var i = 0; i < listOfCommits.length; i++) {
      if (commit.equals(listOfCommits[i].getId().getName())) {
        commitItem = listOfCommits[i];
      }
    }

    return commitItem.getDiffFiles();
  }

  public List<String> availableRepositories() {

    return FileUtil.listOfFolders(settingsExec.folderOfMetrics());
  }

  public MetricsData metricsFromDiffFilesInACommit(String repository, String branch, String commit)
      throws GitRepositoryException, GitAPIException {

    var resourceDiff = Resource
      .create(repository, ResourceType.METRICS_DIFF_FILES)
      .folder("commits")
      .name(commit);

    if (!resourceDiff.exists()) {
      metricExec.analyzeDiffFilesInACommit(repository, branch, commit);
    }

    return resourceDiff.read(MetricsData.class);
  }

  public MetricsData metricsFromAllFilesInACommit(String repository, String branch, String commit)
      throws GitRepositoryException, GitAPIException {

    var resourceAll = Resource
      .create(repository, ResourceType.METRICS_ALL_FILES)
      .folder("commits")
      .name(commit);

    if (!resourceAll.exists()) {
      metricExec.analyzeAllFilesInACommit(repository, branch, commit);
    }

    return resourceAll.read(MetricsData.class);
  }

  public HashMap<String, MetricsData> metricsFromDiffFilesInARangeOfCommits(String repository, String branch,
      Commit[] commits) throws GitRepositoryException, GitAPIException {

    var map = new HashMap<String, MetricsData>();
    for (var commit : commits) {

      var result = metricsFromDiffFilesInACommit(repository, branch, commit.getId().getName());

      map.put(commit.getId().getName(), result);
    }

    return map;
  }

  public HashMap<String, MetricsData> metricsFromAllFilesInARangeOfCommits(String repository, String branch,
      Commit[] commits) throws GitRepositoryException, GitAPIException {

    var map = new HashMap<String, MetricsData>();
    for (var commit : commits) {
      var result = metricsFromAllFilesInACommit(repository, branch, commit.getId().getName());

      map.put(commit.getId().getName(), result);
    }

    return map;
  }

  public void download(String repository, String url) throws GitRepositoryException {

    repoExec.download(repository, url);
  }

  Stats stats(String repository) {

    return new Stats(this, repository);
  }

  public Map<String, Map<String, Double>> averageOfMetricsOfFiles(
    String repository,
    String branch,
    Integer from, 
    Integer to,
    Integer step
  ) throws GitRepositoryException, GitAPIException {
    
    var stats = stats(repository);
    return stats.extractValuesFromCommits(
      stats.data(branch,from,to,step),
      stats.getAverageOfMetricsOfFiles()
    );
  }

  public Map<String, Map<String, Double>> sumOfMetricsOfFiles (
    String repository,
    String branch,
    Integer from, 
    Integer to,
    Integer step
  ) throws GitRepositoryException, GitAPIException {
    
    var stats = stats(repository);
    return stats.extractValuesFromCommits(
      stats.data(branch,from,to,step),
      stats.getSumOfMetricsOfFiles()
    );
  }

  public Map<String, Map<String, Double>> normalizedSumOfMetricsOfFiles (
    String repository,
    String branch,
    Integer from, 
    Integer to,
    Integer step
  ) throws GitRepositoryException, GitAPIException {

    var data = sumOfMetricsOfFiles(repository, branch, from, to, step);
    return stats(repository).normalizeMetrics(data);
  }

  public Map<String, Map<String, Double>> normalizedAverageOfMetricsOfFiles (
    String repository,
    String branch,
    Integer from, 
    Integer to,
    Integer step
  ) throws GitRepositoryException, GitAPIException {

    var data = averageOfMetricsOfFiles(repository, branch, from, to, step);
    return stats(repository).normalizeMetrics(data);
  }

  public Map<String, Map<String, Double>> evolutionOfFilesByMetrics (
    String repository,
    String branch,
    Integer from, 
    Integer to,
    Integer step,
    String metric
  ) throws GitRepositoryException, GitAPIException {

    var stats = stats(repository);
    var data = stats.data(branch,from,to,step);
    
    var result = new HashMap<String,Map<String,Double>>();
    data.forEach((commit, files) -> {
      result.put(
        commit,
        stats.getEvolution().apply(files.getFiles(), metric)
      );
    });
    return result;
  }
}
