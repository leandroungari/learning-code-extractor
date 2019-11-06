package core;

import java.util.List;

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
import core.result.Map;
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

  /**
   * It lists all branches in the specified repository
   * @param repositoryName The name of repository
   * @return List of branches
   * @throws GitAPIException
   * @throws GitRepositoryException
   */
  public Branch[] branches(String repositoryName) throws GitAPIException, GitRepositoryException {

    var resource = Resource.create(repositoryName, ResourceType.LIST_OF_BRANCHES);

    if (!resource.exists()) {
      repoExec.branches(repositoryName);
    }

    return resource.read(Branch[].class);
  }

  /**
   * This method lists the commits in a specified branch
   * @param repository Name of repository
   * @param branch  The ID (hash) of branch
   * @return List of commits
   * @throws GitAPIException
   * @throws GitRepositoryException
   */
  public Commit[] commits(String repository, String branch) throws GitAPIException, GitRepositoryException {

    var resource = Resource.create(repository, ResourceType.LIST_OF_COMMITS).branch(branch);

    if (!resource.exists()) {
      repoExec.commits(repository, branch);
    }

    return resource.read(Commit[].class);
  }

  /**
   * It gets some description information of a particular commit
   * @param repository Name of repository
   * @param branch Branch ID
   * @param commit Commit ID
   * @return
   * @throws GitAPIException
   * @throws GitRepositoryException
   */
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

  /**
   * It lists the modified files in a particular commit
   * @param repository Name of repository
   * @param branch Branch ID
   * @param commit Commit ID
   * @return List of modified files
   * @throws GitRepositoryException
   * @throws GitAPIException
   */
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

  /**
   * It lists the available repositories to metric's analysis
   * @return List of names of repositories
   */
  public List<String> availableRepositories() {

    return FileUtil.listOfFolders(settingsExec.folderOfMetrics());
  }

  /**
   * It extracts the metrics from the modified files in a commit
   * @param repository Name of repository
   * @param branch Branch ID
   * @param commit Commit ID
   * @return The data of metrics in a commit
   * @throws GitRepositoryException
   * @throws GitAPIException
   */
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

  /**
   * It extracts the metrics of all files in a commit
   * @param repository Name of repository
   * @param branch Branch ID
   * @param commit Commit ID
   * @return The data of metrics in a commit
   * @throws GitRepositoryException
   * @throws GitAPIException
   */
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

  /**
   * It extracts the metrics from diff files in a range of commits
   * @param repository Name of repository
   * @param branch Branch ID
   * @param commits List of commit's IDs
   * @return The data of metrics in each commit
   * @throws GitRepositoryException
   * @throws GitAPIException
   */
  public Map<String, MetricsData> metricsFromDiffFilesInARangeOfCommits(String repository, String branch,
      Commit[] commits) throws GitRepositoryException, GitAPIException {

    var map = new Map<String, MetricsData>();
    for (var commit : commits) {

      var result = metricsFromDiffFilesInACommit(repository, branch, commit.getId().getName());

      map.add(commit.getId().getName(), result);
    }

    return map;
  }

  /**
   * It extracts the metrics from all files in a range of commits
   * @param repository Name of repository
   * @param branch Branch ID
   * @param commits List of commit's IDs
   * @return The data of metrics in each commit
   * @throws GitRepositoryException
   * @throws GitAPIException
   */
  public Map<String, MetricsData> metricsFromAllFilesInARangeOfCommits(String repository, String branch,
      Commit[] commits) throws GitRepositoryException, GitAPIException {

    var map = new Map<String, MetricsData>();
    for (var commit : commits) {
      var result = metricsFromAllFilesInACommit(repository, branch, commit.getId().getName());

      map.add(commit.getId().getName(), result);
    }

    return map;
  }

  /**
   * It performs the download of the Git repository from the specified URL
   * @param repository Name of repository
   * @param url The URL address to clone the repository
   * @throws GitRepositoryException
   */
  public void download(String repository, String url) throws GitRepositoryException {

    repoExec.download(repository, url);
  }

  Stats stats() {

    return new Stats(this);
  }

  public Map<String, Map<String, Double>> averageOfMetricsOfFiles(
    String repository,
    String branch,
    Integer from, 
    Integer to,
    Integer step
  ) throws GitRepositoryException, GitAPIException {
    
    var repo = stats().repository(repository);
    return repo.extractValuesFromCommits(
      repo.data(branch,from,to,step),
      repo.getAverageOfMetricsOfFiles()
    );
  }

  public Map<String, Map<String, Double>> sumOfMetricsOfFiles (
    String repository,
    String branch,
    Integer from, 
    Integer to,
    Integer step
  ) throws GitRepositoryException, GitAPIException {
    
    var repo = stats().repository(repository);
    return repo.extractValuesFromCommits(
      repo.data(branch,from,to,step),
      repo.getSumOfMetricsOfFiles()
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
    return stats().repository(repository).normalizeMetrics(data);
  }

  public Map<String, Map<String, Double>> normalizedAverageOfMetricsOfFiles (
    String repository,
    String branch,
    Integer from, 
    Integer to,
    Integer step
  ) throws GitRepositoryException, GitAPIException {

    var data = averageOfMetricsOfFiles(repository, branch, from, to, step);
    return stats()
      .repository(repository)
      .normalizeMetrics(data);
  }

  public Map<String, Map<String, Double>> evolutionOfFilesByMetrics (
    String repository,
    String branch,
    Integer from, 
    Integer to,
    Integer step,
    String metric
  ) throws GitRepositoryException, GitAPIException {

    var repo = stats().repository(repository);
    var data = repo.data(branch,from,to,step);
    
    var result = new Map<String,Map<String,Double>>();
    data.entries().forEach((commit, files) -> {
      result.add(
        commit,
        repo.getEvolution().apply(files.getFiles(), metric)
      );
    });
    return result;
  }
}
