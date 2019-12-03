package core.features;

import java.util.HashMap;
import java.util.Map;

public class GetEvolutionOfFilesByMetrics 
  implements Feature<HashMap<String,HashMap<String,Double>>> {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;
  private String metric;

  @Override
  public Feature<HashMap<String,HashMap<String,Double>>> setParams(String... params) {
    
    repository = params[0];
    branch = params[1];
    from = Integer.valueOf(params[2]);
    to = Integer.valueOf(params[3]);
    metric = params[4];

    return this;
  }

  @Override
  public HashMap<String,HashMap<String,Double>> action() {
    
    var metrics = getMetricsOfCommits();
    var files = extractAllExistingFiles(metrics);

    return extractMetricOfAllFiles(metrics, files);
  }

  private HashMap<String,HashMap<String,Double>> extractMetricOfAllFiles(
    Map<String, Map<String, Map<String, Double>>> data,
    Map<String, Double> files) {

    var result = new HashMap<String,HashMap<String,Double>>();
    data.forEach((id, commit) -> {
      var instanceOfFiles = cloneMap(files);
      commit.forEach((file,metrics) -> {
        instanceOfFiles.put(file, metrics.get(metric));
      });
    });

    return result;
  }

  private Map<String, Map<String, Map<String, Double>>> getMetricsOfCommits() {
    
    return player
      .<Map<String,Map<String,Map<String, Double>>>>get(FeatureType.METRICS_IN_RANGE_COMMITS)
      .setParams(repository,branch,from.toString(),to.toString(),metric)
      .action();
  }

  private Map<String, Double> extractAllExistingFiles(
    Map<String, Map<String, Map<String, Double>>> metrics) {

    var listOfFiles = new HashMap<String,Double>();
    metrics.values().forEach(commit -> {
      commit.forEach((file,values) -> {
        listOfFiles.put(file, 0d);
      });
    });
    return listOfFiles;
  }

  private HashMap<String, Double> cloneMap(Map<String, Double> map) {
    
    var result = new HashMap<String,Double>();

    map.forEach((file,value) -> {
      result.put(file, value);
    });

    return result;
  }

  
}