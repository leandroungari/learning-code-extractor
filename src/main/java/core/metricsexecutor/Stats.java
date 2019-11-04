package core.metricsexecutor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.Player;
import repository.exception.GitRepositoryException;

public class Stats {

  public static final Integer CBO = 0;
  public static final Integer DIT = 1;
  public static final Integer NOSI = 4;
  public static final Integer RFC = 5;
  public static final Integer WMC = 6;

  private static Function<HashMap<String,Double[]>,Double[]>
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

  private static Function<HashMap<String,Double[]>,Double[]> 
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

  private static BiFunction<HashMap<String,Double[]>,String,HashMap<String,Double>>
    evolution = (files, metric) -> {
    
    final Integer position = chooseMetric(metric);
    var result = new HashMap<String,Double>();
    files.forEach((file, metrics) -> {
      result.put(file,metrics[position]);
    });
    
    return result;
  };

  private String repository;
  private Player player;

  public Stats(Player player, String repo) {
    this.repository = repo;
    this.player = player;
  }

  private static Integer chooseMetric(String name) {
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

  public static Function<HashMap<String, Double[]>, Double[]> getSumOfMetricsOfFiles() {
    return sumOfMetricsOfFiles;
  }

  public static Function<HashMap<String, Double[]>, Double[]> getAverageOfMetricsOfFiles() {
    return averageOfMetricsOfFiles;
  }

  public static BiFunction<HashMap<String, Double[]>, String, HashMap<String, Double>> getEvolution() {
    return evolution;
  }

  public BranchStats branch() {
    return new BranchStats(player, repository);
  }

  public CommitStats commits() {
    
  }
  
}
