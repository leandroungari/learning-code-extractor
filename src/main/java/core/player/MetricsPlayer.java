package core.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.Player;
import core.data.Commit;
import core.data.MetricsData;
import core.resource.Resource;
import core.resource.ResourceType;
import repository.exception.GitRepositoryException;
import util.FileUtil;

public class MetricsPlayer extends Player {

  private String nameOfRepository;

  public MetricsPlayer(String nameOfRepository) {
    super();
    this.nameOfRepository = nameOfRepository;
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
  public MetricsData metricsFromDiffFilesInACommit(
    String branch, 
    String commit)
      throws GitRepositoryException, GitAPIException {

    var resourceDiff = Resource
    .create(
      nameOfRepository, 
      ResourceType.METRICS_DIFF_FILES
    )
    .folder("commits")
    .name(commit);

    if (!resourceDiff.exists()) {
      metricsExecutor
      .analyzeDiffFilesInACommit(nameOfRepository, branch, commit);
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
  public MetricsData metricsFromAllFilesInACommit(
    String branch, 
    String commit)
      throws GitRepositoryException, 
             GitAPIException {

    var resourceAll = Resource
      .create(nameOfRepository, ResourceType.METRICS_ALL_FILES)
      .folder("commits")
      .name(commit);

    if (!resourceAll.exists()) {
      metricsExecutor
      .analyzeAllFilesInACommit(nameOfRepository, branch, commit);
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
  public Map<String, MetricsData> metricsFromDiffFilesInARangeOfCommits(
    String branch,
    Commit[] commits) 
      throws GitRepositoryException, 
             GitAPIException {

    var map = new HashMap<String, MetricsData>();
    for (var commit : commits) {
      var result = metricsFromDiffFilesInACommit(
        branch, 
        commit.getId().getName()
      );
      map.put(commit.getId().getName(),result);
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
  public Map<String, MetricsData> metricsFromAllFilesInARangeOfCommits(
    String branch,
    Commit[] commits) 
      throws GitRepositoryException, 
             GitAPIException {

    var map = new HashMap<String, MetricsData>();
    for (var commit : commits) {
      var result = metricsFromAllFilesInACommit(
        branch, 
        commit.getId().getName()
      );

      map.put(commit.getId().getName(), result);
    }

    return map;
  }

  /**
   * It lists the available repositories to metric's analysis
   * @return List of names of repositories
   */
  public List<String> availableRepositories() {

    return FileUtil.listOfFolders(settingsExecutor.folderOfMetrics());
  }

  public Map<String, List<Map<String,Double>>> relevant(
    String branch,
    String metric,
    String option) 
      throws GitAPIException, GitRepositoryException {
    
    return metricsExecutor
    .relevantCommits(
      nameOfRepository,
      branch, 
      metric, 
      option
    );
  }

  public Map<String, Map<String, Double>> averageOfMetricsOfFiles(
    String branch,
    Integer from, 
    Integer to,
    Integer step
  ) throws GitRepositoryException, GitAPIException {
    
    var repo = stats().repository(nameOfRepository);
    return repo.extractValuesFromCommits(
      repo.data(branch,from,to,step),
      repo.getAverageOfMetricsOfFiles()
    );
  }

  public Map<String, Map<String, Double>> sumOfMetricsOfFiles (
    String branch,
    Integer from, 
    Integer to,
    Integer step
  ) throws GitRepositoryException, GitAPIException {
    
    var repo = stats().repository(nameOfRepository);
    return repo.extractValuesFromCommits(
      repo.data(branch,from,to,step),
      repo.getSumOfMetricsOfFiles()
    );
  }

  public Map<String, Map<String, Double>> normalizedSumOfMetricsOfFiles (
    String branch,
    Integer from, 
    Integer to,
    Integer step
  ) throws GitRepositoryException, GitAPIException {

    var data = sumOfMetricsOfFiles(
      branch, 
      from, 
      to, 
      step
    );

    return stats()
      .repository(nameOfRepository)
      .normalizeMetrics(data);
  }

  public Map<String, Map<String, Double>> normalizedAverageOfMetricsOfFiles (
    String branch,
    Integer from, 
    Integer to,
    Integer step
  ) throws GitRepositoryException, GitAPIException {

    var data = averageOfMetricsOfFiles(branch, from, to, step);
    return stats()
      .repository(nameOfRepository)
      .normalizeMetrics(data);
  }

  public Map<String, Map<String, Double>> evolutionOfFilesByMetrics (
    String branch,
    Integer from, 
    Integer to,
    Integer step,
    String metric
  ) throws GitRepositoryException, GitAPIException {

    var repo = stats().repository(nameOfRepository);
    var data = repo.data(branch,from,to,step);
    
    var result = new HashMap<String,Map<String,Double>>();
    data.forEach((commit,files) -> {
      result.put(
        commit,
        repo.getEvolution().apply(files.getFiles(), metric)
      );
    });
    return result;
  }
}