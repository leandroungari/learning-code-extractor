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

    return player.metricsFromAllFilesInACommit(repository, branch, commit);
  }

  @GetMapping("/metrics/diff/{repository}/{branch}/{commit}")
  @ResponseBody
  public MetricsData metricsDiffInACommit(@PathVariable String repository, @PathVariable String branch,
      @PathVariable String commit) throws GitRepositoryException, GitAPIException {

    return player.metricsFromDiffFilesInACommit(repository, branch, commit);
  }

  @GetMapping("/metrics/all/{repository}/{branch}/{from}/{to}")
  @ResponseBody
  public Map<String, MetricsData> metricsAllInARangeOfCommits(@PathVariable String repository,
      @PathVariable String branch, @PathVariable Integer from, @PathVariable Integer to)
      throws GitRepositoryException, GitAPIException {

    var resultSet = new ArrayList<Commit>();
    var listOfCommits = player.commits(repository, branch);

    for (var i = 0; i < listOfCommits.length; i++) {
      if (from <= i && i <= to) {
        resultSet.add(listOfCommits[i]);
      } else if (i > to)
        break;
    }

    return player
      .metricsFromAllFilesInARangeOfCommits(
        repository, 
        branch, 
        resultSet.toArray(new Commit[resultSet.size()])
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

    var resultSet = new ArrayList<Commit>();
    var listOfCommits = player.commits(repository, branch);

    for (var i = 0; i < listOfCommits.length; i++) {
      if (from <= i && i <= to) {
        resultSet.add(listOfCommits[i]);
      } else if (i > to)
        break;
    }

    return player
      .metricsFromDiffFilesInARangeOfCommits(
        repository, 
        branch, 
        resultSet.toArray(new Commit[resultSet.size()])
      );

	}

	@GetMapping("/metrics/availableRepositories")
	@ResponseBody
	public List<String> list() {

    return player
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
      .relevant(repository, branch, metric, option);
  }

}
