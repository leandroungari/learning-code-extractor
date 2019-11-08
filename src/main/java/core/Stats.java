package core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import core.stats.RepositoryStats;

public class Stats {

  public static final Integer CBO = 0;
  public static final Integer DIT = 1;
  public static final Integer NOSI = 4;
  public static final Integer RFC = 5;
  public static final Integer WMC = 6;

  protected Player player;

  private Function<Map<String, Double[]>, Double[]> sumOfMetricsOfFiles = files -> {

    var sumOfMetrics = new Double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
    
    files.forEach((name,metrics) -> {
      sumOfMetrics[0] += metrics[CBO];
      sumOfMetrics[1] += metrics[DIT];
      sumOfMetrics[2] += metrics[NOSI];
      sumOfMetrics[3] += metrics[RFC];
      sumOfMetrics[4] += metrics[WMC];
    });

    return sumOfMetrics;
  };

  private Function<Map<String, Double[]>, Double[]> averageOfMetricsOfFiles = files -> {

    var sum = sumOfMetricsOfFiles.apply(files);
    var size = files.size();
    return new Double[] { sum[0] / (size != 0 ? size : 1), sum[1] / (size != 0 ? size : 1),
        sum[2] / (size != 0 ? size : 1), sum[3] / (size != 0 ? size : 1), sum[4] / (size != 0 ? size : 1), };
  };

  private BiFunction<Map<String, Double[]>, String, Map<String, Double>> evolution = (files, metric) -> {

    final Integer position = chooseMetric(metric);
    var result = new HashMap<String, Double>();
    files.forEach((file,metrics) -> {
      result.put(file, metrics[position]);
    });
    
    return result;
  };

  public Stats(Player player) {
    this.player = player;
  }

  protected static Integer chooseMetric(String name) {
    switch (name) {
    case "cbo":
      return CBO;
    case "dit":
      return DIT;
    case "nosi":
      return NOSI;
    case "rfc":
      return RFC;
    case "wmc":
      return WMC;
    }
    return 0;
  }

  public Function<Map<String, Double[]>, Double[]> getSumOfMetricsOfFiles() {
    return sumOfMetricsOfFiles;
  }

  public Function<Map<String, Double[]>, Double[]> getAverageOfMetricsOfFiles() {
    return averageOfMetricsOfFiles;
  }

  public BiFunction<Map<String, Double[]>, String, Map<String, Double>> getEvolution() {
    return evolution;
  }

  protected Map<String, Map<String, Double>> normalizeMetrics(Map<String, Map<String, Double>> data) {

    Map<String, Double[]> stats = extractStats(data);
    var cbo = stats.get("cbo");
    var dit = stats.get("dit");
    var nosi = stats.get("nosi");
    var rfc = stats.get("rfc");
    var wmc = stats.get("wmc");

    var result = new HashMap<String, Map<String, Double>>();
    data.forEach((file,metrics) -> {
      Map<String, Double> values = new HashMap<>();
      values.put("cbo", (metrics.get("cbo") - cbo[0]) / (cbo[1] - cbo[0] != 0 ? cbo[1] - cbo[0] : 1));
      values.put("dit", (metrics.get("dit") - dit[0]) / (dit[1] - dit[0] != 0 ? dit[1] - dit[0] : 1));
      values.put("nosi", (metrics.get("nosi") - nosi[0]) / (nosi[1] - nosi[0] != 0 ? nosi[1] - nosi[0] : 1));
      values.put("wmc", (metrics.get("wmc") - wmc[0]) / (wmc[1] - wmc[0] != 0 ? wmc[1] - wmc[0] : 1));
      values.put("rfc", (metrics.get("rfc") - rfc[0]) / (rfc[1] - rfc[0] != 0 ? rfc[1] - rfc[0] : 1));
      
      result.put(file,values);
    });

    return result;
  }

  Map<String,Double[]> extractStats(Map<String,Map<String,Double>> result) {
    
    var b = new HashMap<String,Double[]>();
    
    b.put(
      "cbo", 
      normalize(result.values().stream().map(a -> a.get("cbo")).collect(Collectors.toList()))
    );
    b.put(
      "dit", 
      normalize(result.values().stream().map(a -> a.get("dit")).collect(Collectors.toList()))
    );
    b.put(
      "nosi", 
      normalize(result.values().stream().map(a -> a.get("nosi")).collect(Collectors.toList()))
    );
    b.put(
      "rfc", 
      normalize(result.values().stream().map(a -> a.get("rfc")).collect(Collectors.toList()))
    );
    b.put(
      "wmc", normalize(result.values().stream().map(a -> a.get("wmc")).collect(Collectors.toList()))
    );  

    return b;
  }

  protected Double[] normalize(List<Double> list) {

    var min = 0.0;
    var max = 0.0;
    try {
      min = list.stream().min(Double::compare).get();
      max = list.stream().max(Double::compare).get();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new Double[]{min,max};
  }

  /**
   * It provide access to analysis methods related to a repository
   * @param repository Name of repository
   * @return Instance of repository analysis class
   */
  public RepositoryStats repository(
    String repository) {

    return new RepositoryStats(player,repository);
  }

  protected Map<String,Double> derivateMetrics(
    Double[] start, 
    Double[] end, 
    Integer size) {
    
    var result = new HashMap<String,Double>();
    
    result.put("cbo", ((end[0] - start[0])/size));
    result.put("dit", ((end[1] - start[1])/size));
    result.put("nosi", ((end[2] - start[2])/size));
    result.put("rfc", ((end[3] - start[3])/size));
    result.put("wmc", ((end[4] - start[4])/size));
      
    return result;
  }
  
}
