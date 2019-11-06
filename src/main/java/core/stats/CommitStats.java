package core.stats;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.Player;
import core.ResultItem;
import core.Stats;
import repository.exception.GitRepositoryException;

public class CommitStats extends Stats {

  private String branch;

  public CommitStats(Player player, String repository, String branch) {
    super(player, repository);
    this.branch = branch;
  }

  public Map<String,ResultItem> derivate() {

    Map<String,ResultItem> result = new HashMap<>();

    try {
      
      var commits = player.commits(repository, branch);
      var data = player.sumOfMetricsOfFiles(repository, branch, 0, commits.length-1, 1);

      for(var i = commits.length-1; i > 0; i--) {
        
        var nextId = commits[i-1].getId().getName();
        var prevId = commits[i].getId().getName();

        var nextData = data.get(nextId).values();
        var prevData = data.get(prevId).values();
        
        result.put(
          nextId,
          derivateMetrics(
            prevData.toArray(new Double[prevData.size()]),
            nextData.toArray(new Double[nextData.size()]),
            commits.length-1
          )
        );
      }

    } catch (GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }

    return result;
  }
}
