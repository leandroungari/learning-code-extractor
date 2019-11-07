package core.stats;

import core.Player;
import core.result.Element;
import core.result.Map;

public class BranchStats extends RepositoryStats {

  private String branch;

  public BranchStats(Player player, String repository, String branch) {
    super(player, repository);
    this.branch = branch;
  }

  public Map<String,Map<String,Element<Double>>> derivate() {

    Map<String,Map<String,Element<Double>>> result = new Map<>();

    try {
      
      var commits = player.commits(repository, branch);
      var data = player.sumOfMetricsOfFiles(repository, branch, 0, commits.length-1, 1);

      for(var i = commits.length-1; i > 0; i--) {
        
        var nextId = commits[i-1].getId().getName();
        var prevId = commits[i].getId().getName();

        var nextData = data.get(nextId).values();
        var prevData = data.get(prevId).values();
        
        result.add(
          nextId,
          derivateMetrics(
            prevData.toArray(),
            nextData.toArray(),
            commits.length-1
          )
        );
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }
}
