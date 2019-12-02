package api.controller;

import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import core.Player;
import core.data.MetricsData;
import core.features.FeatureType;
import repository.exception.GitRepositoryException;

@RestController()
public class Metrics {

  private static Player player = new Player();

  @GetMapping("/metrics/{repository}/{branch}/{commit}")
  @ResponseBody
  public MetricsData metricsInACommit(
    @PathVariable String repository, 
    @PathVariable String branch,
    @PathVariable String commit) 
      throws GitRepositoryException, 
             GitAPIException {

    return player
      .<MetricsData>get(FeatureType.METRICS_IN_COMMIT)
      .setParams(repository, branch, commit)
      .action();
  }

  @GetMapping("/metrics/{repository}/{branch}/{from}/{to}")
  @ResponseBody
  public Map<String, MetricsData> metricsInARangeOfCommits(
    @PathVariable String repository,
    @PathVariable String branch, 
    @PathVariable String from, 
    @PathVariable String to)
      throws GitRepositoryException, GitAPIException {

    return player
      .<Map<String,MetricsData>>get(FeatureType.METRICS_IN_RANGE_COMMITS)
      .setParams(repository,branch,from,to)
      .action();
  }

  @GetMapping("/metrics/relevant/{repository}/{branch}/{metric}/{option}")
  @ResponseBody
  public Map<String, List<Map<String,Double>>> relevant(
    @PathVariable String repository,
    @PathVariable String branch,
    @PathVariable String metric,
    @PathVariable String option) 
      throws GitAPIException, GitRepositoryException {

    return player
      .<Map<String, List<Map<String,Double>>>>get(FeatureType.RELEVANT_COMMITS_BY_METRIC)
      .setParams(repository, branch, metric, option)
      .action();
  }

}
