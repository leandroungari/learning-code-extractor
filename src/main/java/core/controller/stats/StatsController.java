package core.controller.stats;

import java.util.HashMap;
import java.util.Map;

public class StatsController {

  public StatsController() {
    
  }


  private HashMap<String,Double> createMap() {
    var map = new HashMap<String,Double>();
    map.put("cbo", 0d);
    map.put("dit", 0d);
    map.put("nosi", 0d);
    map.put("rfc", 0d);
    map.put("wmc", 0d);
    return map;
  }

  public Map<String,Double> sumOfMetrics(Map<String,Map<String,Double>> metricsFromCommit) {

    var sumOfMetrics = createMap();

    metricsFromCommit.forEach((name,metrics) -> {
      
      sumOfMetrics.put("cbo", sumOfMetrics.get("cbo") + metrics.get("cbo"));
      sumOfMetrics.put("dit", sumOfMetrics.get("dit") + metrics.get("dit"));
      sumOfMetrics.put("nosi", sumOfMetrics.get("nosi") + metrics.get("nosi"));
      sumOfMetrics.put("rfc", sumOfMetrics.get("rfc") + metrics.get("rfc"));
      sumOfMetrics.put("wmc", sumOfMetrics.get("wmc") + metrics.get("wmc"));
    });

    return sumOfMetrics;
  };

  public Map<String, Double> averageOfMetrics(Map<String,Map<String,Double>> metricsFromCommit) {

    var sum = sumOfMetrics(metricsFromCommit);
    var size = metricsFromCommit.size();
    return Map.of(
      "cbo", sum.get("cbo")/(size != 0 ? size : 1),
      "dit", sum.get("dit")/(size != 0 ? size : 1),
      "nosi", sum.get("nosi")/(size != 0 ? size : 1),
      "rfc", sum.get("rfc")/(size != 0 ? size : 1),
      "wmc", sum.get("wmc")/(size != 0 ? size : 1)
    );
  }

  public Map<String,Map<String,Double>> normalizeMetrics(
    Map<String,Map<String,Double>> metrics) {

    var command = new NormalizeMetrics(metrics);
    return command.execute();
  }

  
}