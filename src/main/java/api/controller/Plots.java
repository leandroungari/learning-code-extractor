package api.controller;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import core.Player;
import core.features.FeatureType;
import repository.exception.GitRepositoryException;

@RestController
public class Plots {

  private static Player player = new Player();

  @GetMapping("/plots/averageMetricsFiles/{repository}/{branch}/{from}/{to}/{step}")
  @ResponseBody
  public Map<String,Map<String,Double>> averageMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable String from, 
    @PathVariable String to,
    @PathVariable String step)
      throws GitRepositoryException, 
             GitAPIException {

    return player
      .<Map<String,Map<String,Double>>>get(FeatureType.AVERAGE_OF_METRICS_OF_FILES)
      .setParams(repository,branch,from,to,step)
      .action();
  }

  @GetMapping("/plots/sumMetricsFiles/{repository}/{branch}/{from}/{to}/{step}")
  @ResponseBody
  public Map<String,Map<String,Double>> sumMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable String   from, 
    @PathVariable String   to,
    @PathVariable String   step)
      throws GitRepositoryException, 
             GitAPIException {

    return player
      .<Map<String,Map<String,Double>>>get(FeatureType.SUM_OF_METRICS_OF_FILES)
      .setParams(repository,branch,from,to,step)
      .action();
  }

  @GetMapping("/plots/sumMetricsFiles/{repository}/{branch}/{from}/{to}/{step}/normalized")
  @ResponseBody
  public Map<String,Map<String,Double>> normalizedSumMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable String from, 
    @PathVariable String to,
    @PathVariable String step)
      throws GitRepositoryException, 
             GitAPIException {

    return player
      .<Map<String,Map<String,Double>>>get(FeatureType.NORMALIZED_SUM_OF_METRICS_OF_FILES)
      .setParams(repository,branch,from,to,step)
      .action();
  }

  @GetMapping("/plots/averageMetricsFiles/{repository}/{branch}/{from}/{to}/{step}/normalized")
  @ResponseBody
  public Map<String,Map<String,Double>> normalizedAverageMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable String from, 
    @PathVariable String to,
    @PathVariable String step)
      throws GitRepositoryException, 
             GitAPIException {

    return player
      .<Map<String,Map<String,Double>>>get(FeatureType.NORMALIZED_AVERAGE_OF_METRICS_OF_FILES)
      .setParams(repository,branch,from,to,step)
      .action();
  }

  @GetMapping("/plots/evolution/{repository}/{branch}/{from}/{to}/{step}/{metric}")
  @ResponseBody
  public HashMap<String,HashMap<String,Double>> evolutionOfFilesByMetrics(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable String from, 
    @PathVariable String to,
    @PathVariable String step,
    @PathVariable String metric)
      throws GitRepositoryException, 
             GitAPIException {

    return player
      .<HashMap<String,HashMap<String,Double>>>get(FeatureType.EVOLUTION_OF_FILES_BY_METRICS)
      .setParams(repository,branch,from,to,step,metric)
      .action();
  }
}
