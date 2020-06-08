package controller;

import metrics.QualityMetricsExtractor;

public class MetricsController {

  private String repository;
  private String branch;

  public MetricsController(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
  }

  public Boolean extract() {
    var extractor = new QualityMetricsExtractor(repository, branch);
    extractor.extract();
    return true;
  }
  
}