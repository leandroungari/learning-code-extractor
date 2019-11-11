package core.stats;

import java.util.HashMap;
import java.util.Map;

import core.Player;

public class BranchStats extends RepositoryStats {

  private String branch;

  public BranchStats(Player player, String repository, String branch) {
    super(player, repository);
    this.branch = branch;
  }

  public Map<String,Map<String,Double>> derivate(String option) {

    Map<String,Map<String,Double>> result = new HashMap<>();

    try {
      
      var commits = player.commits(repository, branch);

      Map<String,Map<String,Double>> data = null;
      
      if(SUM_OPTION.equals(option)) {
        
        data = player.sumOfMetricsOfFiles(
          repository, 
          branch, 
          0, 
          commits.length-1, 
          1
        );
      }
      else {
        
        data = player.averageOfMetricsOfFiles(
          repository, 
          branch, 
          0, 
          commits.length-1, 
          1
        );
      }

      for(var i = commits.length-1; i > 0; i--) {
        
        var nextId = commits[i-1].getId().getName();
        var prevId = commits[i].getId().getName();

        var nextData = data.get(nextId).values();
        var prevData = data.get(prevId).values();
        
        result.put(
          nextId,
          derivateMetrics(
            prevData.toArray(new Double[prevData.size()]),
            nextData.toArray(new Double[prevData.size()]),
            1
          )
        );
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }
}
