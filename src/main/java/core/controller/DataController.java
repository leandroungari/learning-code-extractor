package core.controller;

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

  public MetricsData metrics(String repository, String commit) {

    var resourceAll = Resource
      .create(repository, ResourceType.METRICS_ALL_FILES)
      .folder("commits")
      .name(commit);

    if (!resourceAll.exists()) {
      analyzeCommit(repository, commit);
    }

    return resourceAll.read(MetricsData.class);
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

  public Double[] sumOfMetrics(MetricsData data) {

    var sumOfMetrics = new Double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };

    data.getFiles().forEach((name,metrics) -> {
      sumOfMetrics[0] += metrics[CBO];
      sumOfMetrics[1] += metrics[DIT];
      sumOfMetrics[2] += metrics[NOSI];
      sumOfMetrics[3] += metrics[RFC];
      sumOfMetrics[4] += metrics[WMC];
    });

    return sumOfMetrics;
  };

  public Double[] averageOfMetrics(MetricsData data) {

    var sum = sumOfMetrics(data);
    var size = data.getFiles().size();
    return new Double[] { 
      sum[0] / (size != 0 ? size : 1), 
      sum[1] / (size != 0 ? size : 1),
      sum[2] / (size != 0 ? size : 1), 
      sum[3] / (size != 0 ? size : 1), 
      sum[4] / (size != 0 ? size : 1)
    };
  }
}