package core;

import java.util.function.BiFunction;
import java.util.function.Function;
import core.result.Element;
import core.result.List;
import core.result.Map;
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

    files.entries().forEach(item -> {
      sumOfMetrics[0] += item.two()[CBO];
      sumOfMetrics[1] += item.two()[DIT];
      sumOfMetrics[2] += item.two()[NOSI];
      sumOfMetrics[3] += item.two()[RFC];
      sumOfMetrics[4] += item.two()[WMC];
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
    var result = new Map<String, Double>();
    files.entries().forEach(item -> {
      result.add(item.one(), item.two()[position]);
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

    var result = new Map<String, Map<String, Double>>();
    data.entries().forEach(item -> {
      Map<String, Double> values = Map.of();
      result.add(
        item.one(),
        values
        .add("cbo", (item.two().get("cbo") - cbo[0]) / (cbo[1] - cbo[0] != 0 ? cbo[1] - cbo[0] : 1))
        .add("dit", (item.two().get("dit") - dit[0]) / (dit[1] - dit[0] != 0 ? dit[1] - dit[0] : 1))
        .add("nosi", (item.two().get("nosi") - nosi[0]) / (nosi[1] - nosi[0] != 0 ? nosi[1] - nosi[0] : 1))
        .add("rfc", (item.two().get("rfc") - rfc[0]) / (rfc[1] - rfc[0] != 0 ? rfc[1] - rfc[0] : 1))
        .add("wmc", (item.two().get("wmc") - wmc[0]) / (wmc[1] - wmc[0] != 0 ? wmc[1] - wmc[0] : 1)));
    });

    return result;
  }

  Map<String,Double[]> extractStats(Map<String,Map<String,Double>> result) {
    
    return new Map<String,Double[]>()
      .add("cbo", normalize(result.values().map(a -> a.get("cbo"))))
      .add("dit", normalize(result.values().map(a -> a.get("dit"))))
      .add("nosi", normalize(result.values().map(a -> a.get("nosi"))))
      .add("rfc", normalize(result.values().map(a -> a.get("rfc"))))
      .add("wmc", normalize(result.values().map(a -> a.get("wmc"))));  
  }

  protected Double[] normalize(List<Double> list) {

    var min = 0.0;
    var max = 0.0;
    try {
      min = list.min(Double::compare);
      max = list.max(Double::compare);
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

  protected Map<String,Element<Double>> derivateMetrics(
    Double[] start, 
    Double[] end, 
    Integer size) {
    
    var result = new Map<String,Element<Double>>();

    return result
      .add("cbo", new Element<Double>((end[0] - start[0])/size))
      .add("dit", new Element<Double>((end[1] - start[1])/size))
      .add("nosi", new Element<Double>((end[2] - start[2])/size))
      .add("rfc", new Element<Double>((end[3] - start[3])/size))
      .add("wmc", new Element<Double>((end[4] - start[4])/size));
  }
  
}
