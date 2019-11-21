package api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import core.Player;
import core.data.Commit;
import core.data.MetricsData;
import repository.exception.GitRepositoryException;

@RestController()
public class Metrics {

  private static Player player = new Player();

  @GetMapping("/metrics")
  @ResponseBody
  public String index() {

    return "This controller returns results related to code quality metrics.";
  }

  @GetMapping("/metrics/all/{repository}/{branch}/{commit}")
  @ResponseBody
  public MetricsData metricsAllInACommit(
    @PathVariable String repository, 
    @PathVariable String branch,
    @PathVariable String commit) 
      throws GitRepositoryException, GitAPIException {

    return player
      .metrics(repository)
      .metricsFromAllFilesInACommit(branch, commit);
  }

  @GetMapping("/metrics/diff/{repository}/{branch}/{commit}")
  @ResponseBody
  public MetricsData metricsDiffInACommit(
    @PathVariable String repository, 
    @PathVariable String branch,
    @PathVariable String commit) 
      throws GitRepositoryException, 
             GitAPIException {

    return player
      .metrics(repository)
      .metricsFromDiffFilesInACommit(branch, commit);
  }

  private Commit[] rangeOfCommits(
    String repository, 
    String branch, 
    Integer from, 
    Integer to) 
      throws GitAPIException, GitRepositoryException {

    var listOfCommits = player
      .repository(repository)
      .commits(branch);

    var resultSet = new ArrayList<Commit>();
    for (var i = 0; i < listOfCommits.length; i++) {
      if (from <= i && i <= to) {
        resultSet.add(listOfCommits[listOfCommits.length - i]);
      }
    }

    return resultSet.toArray(new Commit[resultSet.size()]);
  }

  @GetMapping("/metrics/all/{repository}/{branch}/{from}/{to}")
  @ResponseBody
  public Map<String, MetricsData> metricsAllInARangeOfCommits(
    @PathVariable String repository,
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to)
      throws GitRepositoryException, GitAPIException {

    return player
      .metrics(repository)
      .metricsFromAllFilesInARangeOfCommits(
        branch, 
        rangeOfCommits(repository, branch, from, to)
      );
  }

  @GetMapping("/metrics/diff/{repository}/{branch}/{from}/{to}")
  @ResponseBody
  public Map<String, MetricsData> metricsDiffInARangeOfCommits(
    @PathVariable String repository,
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to)
      throws GitRepositoryException, GitAPIException {

    return player
      .metrics(repository)
      .metricsFromDiffFilesInARangeOfCommits(
        branch, 
        rangeOfCommits(repository, branch, from, to)
      );
	}

  /**
   * Improve it
   * @return
   */
	@GetMapping("/metrics/availableRepositories")
	@ResponseBody
	public List<String> list() {

    return player
      .metrics("")
      .availableRepositories();
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
      .metrics(repository)
      .relevant(branch, metric, option);
  }

}
