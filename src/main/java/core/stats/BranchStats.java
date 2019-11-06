package core.stats;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.Player;
import core.ResultItem;
import core.Stats;
import core.data.Commit;
import repository.exception.GitRepositoryException;

public class BranchStats extends Stats {

  public BranchStats(Player player, String repository) {
    super(player, repository);
  }

  public Map<String, ResultItem<Double>> derivate() 
    throws GitAPIException, GitRepositoryException {
      
    var branches = player.branches(repository);
    Map<String, ResultItem<Double>> result = new HashMap<>();
    
    for(var branch: branches) {
      var name = branch.getId().getName();
      
      result.put(name,this.execute(name));
    }
    return result;
  }

  private ResultItem<Double> execute(String branch) {
    
    Commit[] commits = null;
    try {
      commits = player.commits(repository, branch);
      return compareInitialAndLastCommit(
        branch,
        commits[commits.length-1].getId().getName(),
        commits[0].getId().getName(),
        commits.length-1
      );
    } catch (GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }
    return null;
  }

  private ResultItem<Double> compareInitialAndLastCommit(
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
