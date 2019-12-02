package core.features;

import java.util.Map;

import core.controller.DataController;
import core.controller.StatsController;

public class GetAverageOfMetricsOfFiles implements Feature<Map<String, Double>> {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;
  private Integer step;

  private StatsController stats = new StatsController();

  @Override
  public Feature<Map<String, Double>> setParams(String... params) {
    
    repository = params[0];
    branch = params[1];
    from = Integer.valueOf(params[2]);
    to = Integer.valueOf(params[3]);
    step = Integer.valueOf(params[4]);

    return this;
  }

  @Override
  public Map<String, Double> action() {
    
    var metrics = getMetricsFromCommits();
    return stats.averageOfMetrics(metrics);
  }

  private Map<String, Map<String, Double>> getMetricsFromCommits() {
    return player
      .<Map<String,Map<String,Double>>>get(FeatureType.METRICS_IN_RANGE_COMMITS)
      .setParams(repository, branch, from.toString(), to.toString(), step.toString())
      .action();
  }

}