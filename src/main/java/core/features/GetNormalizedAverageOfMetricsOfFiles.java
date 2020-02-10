package core.features;

import java.util.HashMap;
import java.util.Map;

import core.controller.StatsController;
import core.data.MetricsData;

public class GetNormalizedAverageOfMetricsOfFiles 
  implements Feature<Map<String,MetricsData>> {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;
  private Integer step;

  private StatsController stats = new StatsController();

  @Override
  public Feature<Map<String,MetricsData>> setParams(String... params) {
    
    repository = params[0];
    branch = params[1];
    from = Integer.valueOf(params[2]);
    to = Integer.valueOf(params[3]);
    step = Integer.valueOf(params[4]);

    return this;
  }

  @Override
  public Map<String,MetricsData> action() {
    
    var result = new HashMap<String,MetricsData>();
    var metrics = getMetricsFromCommits();
    metrics.forEach((commit, files) -> {
      result.put(commit,stats.averageOfMetrics(files));
    });

    return stats.normalizeMetrics(result);
  }

  private Map<String,Map<String, MetricsData>> getMetricsFromCommits() {
    return player
      .<Map<String,Map<String, MetricsData>>>get(FeatureType.METRICS_IN_RANGE_COMMITS)
      .setParams(repository, branch, from.toString(), to.toString(), step.toString())
      .action();
  }

}