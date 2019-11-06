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

  private Function<HashMap<String,Double[]>,Double[]>
    sumOfMetricsOfFiles = files -> {

    var sumOfMetrics = new Double[]{
      0.0,0.0,0.0,0.0,0.0
    };

    files.forEach((file,metrics) -> {
      sumOfMetrics[0] += metrics[CBO];
      sumOfMetrics[1] += metrics[DIT];
      sumOfMetrics[2] += metrics[NOSI];
      sumOfMetrics[3] += metrics[RFC];
      sumOfMetrics[4] += metrics[WMC];
    });

    return sumOfMetrics;
  };

  private Function<HashMap<String,Double[]>,Double[]> 
    averageOfMetricsOfFiles = files -> {
    
    var sum = sumOfMetricsOfFiles.apply(files);
    var size = files.size();
    return new Double[]{
      sum[0]/(size != 0 ? size : 1),
      sum[1]/(size != 0 ? size : 1),
      sum[2]/(size != 0 ? size : 1),
      sum[3]/(size != 0 ? size : 1),
      sum[4]/(size != 0 ? size : 1),
    };
  };

  private BiFunction<HashMap<String,Double[]>,String,HashMap<String,Double>>
    evolution = (files, metric) -> {
    
    final Integer position = chooseMetric(metric);
    var result = new HashMap<String,Double>();
    files.forEach((file, metrics) -> {
      result.put(file,metrics[position]);
    });
    
    return result;
  };

  public Stats(Player player) {
    this.player = player;
  }

  protected static Integer chooseMetric(String name) {
    switch(name) {
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

  public Function<HashMap<String, Double[]>, Double[]> getSumOfMetricsOfFiles() {
    return sumOfMetricsOfFiles;
  }

  public Function<HashMap<String, Double[]>, Double[]> getAverageOfMetricsOfFiles() {
    return averageOfMetricsOfFiles;
  }

  public BiFunction<HashMap<String, Double[]>, String, HashMap<String, Double>> getEvolution() {
    return evolution;
  }

  protected Map<String,Map<String,Double>> normalizeMetrics(Map<String,Map<String,Double>> data) {
    
    Map<String,Double[]> stats = extractStats(data);
    var cbo = stats.get("cbo");
    var dit = stats.get("dit");
    var nosi = stats.get("nosi");
    var rfc = stats.get("rfc");
    var wmc = stats.get("wmc");

    var result = new HashMap<String,Map<String,Double>>();
    data.forEach((commit, metrics) -> {

      result.put(commit, Map.ofEntries(
        Map.entry("cbo", (metrics.get("cbo")-cbo[0])/(cbo[1]-cbo[0] != 0 ? cbo[1]-cbo[0] : 1)),
        Map.entry("dit", (metrics.get("dit")-dit[0])/(dit[1]-dit[0] != 0 ? dit[1]-dit[0] : 1)),
        Map.entry("nosi", (metrics.get("nosi")-nosi[0])/(nosi[1]-nosi[0] != 0 ? nosi[1]-nosi[0] : 1)),
        Map.entry("rfc", (metrics.get("rfc")-rfc[0])/(rfc[1]-rfc[0] != 0 ? rfc[1]-rfc[0] : 1)),
        Map.entry("wmc", (metrics.get("wmc")-wmc[0])/(wmc[1]-wmc[0] != 0 ? wmc[1]-wmc[0] : 1))
      ));
    });

    return result;
  }

  Map<String,Double[]> extractStats(Map<String,Map<String,Double>> result) {
    return Map.ofEntries(
      Map.entry(
        "cbo", 
        normalize(
          result.values().stream()
            .map(a -> a.get("cbo"))
            .collect(Collectors.toList())
        )
      ),
      Map.entry(
        "dit",
        normalize(
          result.values().stream()
            .map(a -> a.get("dit"))
            .collect(Collectors.toList())
        )
      ),
      Map.entry(
        "nosi",
        normalize(
          result.values().stream()
            .map(a -> a.get("nosi"))
            .collect(Collectors.toList()))
      ),
      Map.entry(
        "rfc",
        normalize(
          result.values().stream()
            .map(a -> a.get("rfc"))
            .collect(Collectors.toList())
          )
      ),
      Map.entry(
        "wmc",
        normalize(
          result.values().stream()
            .map(a -> a.get("wmc"))
            .collect(Collectors.toList())
          )
      )
    );
  }

  

  protected Double[] normalize(List<Double> list) {

    var min = list.stream().min(Double::compare).get();
    var max = list.stream().max(Double::compare).get();

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

  protected ResultItem<Double> derivateMetrics(
    Double[] start, 
    Double[] end, 
    Integer size) {
    
    ResultItem<Double> result = ResultItem.create();

    return result
      .add("cbo", Double.valueOf((end[0] - start[0])/size))
      .add("dit", Double.valueOf((end[1] - start[1])/size))
      .add("nosi", Double.valueOf((end[2] - start[2])/size))
      .add("rfc", Double.valueOf((end[3] - start[3])/size))
      .add("wmc", Double.valueOf((end[4] - start[4])/size));
  }
  
}
