package api.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import core.Player;
import core.result.Map;
import repository.exception.GitRepositoryException;

@RestController
public class Plots {

  private static Player player = new Player();

  @GetMapping("/plots/averageMetricsFiles/{repository}/{branch}/{from}/{to}/{step}")
  @ResponseBody
  public Map<String,Map<String,Double>> averageMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to,
    @PathVariable Integer step)
      throws GitRepositoryException, 
             GitAPIException {

    return player
      .averageOfMetricsOfFiles(repository, branch, from, to, step);
  }

  @GetMapping("/plots/sumMetricsFiles/{repository}/{branch}/{from}/{to}/{step}")
  @ResponseBody
  public Map<String,Map<String,Double>> sumMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to,
    @PathVariable Integer step)
      throws GitRepositoryException, 
             GitAPIException {

    return player
      .sumOfMetricsOfFiles(repository, branch, from, to, step);
  }

  @GetMapping("/plots/sumMetricsFiles/{repository}/{branch}/{from}/{to}/{step}/normalized")
  @ResponseBody
  public Map<String,Map<String,Double>> normalizedSumMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to,
    @PathVariable Integer step)
      throws GitRepositoryException, 
             GitAPIException {

    
    return player
      .normalizedSumOfMetricsOfFiles(repository, branch, from, to, step);
  }

  @GetMapping("/plots/averageMetricsFiles/{repository}/{branch}/{from}/{to}/{step}/normalized")
  @ResponseBody
  public Map<String,Map<String,Double>> normalizedAverageMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to,
    @PathVariable Integer step)
      throws GitRepositoryException, 
             GitAPIException {

    return player
      .normalizedAverageOfMetricsOfFiles(repository, branch, from, to, step);
  }

  @GetMapping("/plots/evolution/{repository}/{branch}/{from}/{to}/{step}/{metric}")
  @ResponseBody
  public Map<String,Map<String,Double>> evolutionOfFilesByMetrics(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to,
    @PathVariable Integer step,
    @PathVariable String metric)
      throws GitRepositoryException, 
             GitAPIException {

    return player
      .evolutionOfFilesByMetrics(repository, branch, from, to, step, metric);
  }
}
