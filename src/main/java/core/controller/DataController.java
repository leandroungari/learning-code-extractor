package core.controller;

import java.util.HashMap;
import java.util.Map;

import core.data.MetricsData;
import core.resource.Resource;
import core.resource.ResourceType;
import repository.metrics.MetricsAnalyzer;

public class DataController {

  private static final MetricsAnalyzer analyzer = new MetricsAnalyzer();

  private static final Integer CBO = 0;
  private static final Integer DIT = 1;
  private static final Integer NOSI = 4;
  private static final Integer RFC = 5;
  private static final Integer WMC = 6;

  public Map<String, Map<String, Double>> metrics(String repository, String commit) {

    var resourceAll = Resource
      .create(repository, ResourceType.METRICS_ALL_FILES)
      .folder("commits")
      .name(commit);

    if (!resourceAll.exists()) {
      analyzeCommit(repository, commit);
    }

    var metrics = resourceAll.read(MetricsData.class);
    return convertFileMetricsToMap(metrics);
  }

  private HashMap<String, Map<String, Double>> convertFileMetricsToMap(MetricsData metrics) {
    
    var result = new HashMap<String,Map<String,Double>>();

    metrics.getFiles().forEach((file,data) -> {
      result.put(
        file,
        Map.of(
          "cbo", data[CBO],
          "dit", data[DIT],
          "nosi", data[NOSI],
          "rfc", data[RFC],
          "wmc", data[WMC]
        )
      );
    });

    return result;
  }

  private void analyzeCommit(String repository, String commit) {
    
    var data = new MetricsData();
    
    analyzer
    .repositoryName(repository)
    .enableLogs(false)
    .process(commit, (file, result) -> {
      data.addFiles(file, result);
    });

    Resource
    .create(repository, ResourceType.METRICS_ALL_FILES)
    .folder("commits")
    .name(commit)
    .write(data);
  }
}