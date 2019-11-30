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