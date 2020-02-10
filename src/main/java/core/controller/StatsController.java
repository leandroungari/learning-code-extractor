package core.controller;

import java.util.Map;

import core.controller.stats.NormalizeMetrics;
import core.data.MetricsData;

public class StatsController {

  public StatsController() {
    
  }

  public MetricsData sumOfMetrics(Map<String,MetricsData> metricsFromCommit) {

    var sumOfMetrics = new MetricsData();

    metricsFromCommit.forEach((name,metrics) -> {
      
      sumOfMetrics.setCbo(sumOfMetrics.getCbo() + metrics.getCbo());
      sumOfMetrics.setWmc(sumOfMetrics.getWmc() + metrics.getWmc());
      sumOfMetrics.setNosi(sumOfMetrics.getNosi() + metrics.getNosi());
      sumOfMetrics.setRfc(sumOfMetrics.getRfc() + metrics.getRfc());
      sumOfMetrics.setDit(sumOfMetrics.getDit() + metrics.getDit());
    });

    return sumOfMetrics;
  };

  public MetricsData averageOfMetrics(Map<String,MetricsData> metricsFromCommit) {

    var sum = sumOfMetrics(metricsFromCommit);
    var size = metricsFromCommit.size();
    var result = new MetricsData();

    result.setCbo(sum.getCbo()/(size != 0 ? size : 1));
    result.setDit(sum.getDit()/(size != 0 ? size : 1));
    result.setNosi(sum.getNosi()/(size != 0 ? size : 1));
    result.setRfc(sum.getRfc()/(size != 0 ? size : 1));
    result.setWmc(sum.getWmc()/(size != 0 ? size : 1));
    
    return result;
  }

  public Map<String,MetricsData> normalizeMetrics(
    Map<String,MetricsData> metrics) {

    var command = new NormalizeMetrics(metrics);
    return command.execute();
  }

  
}