package controller;

import metrics.QualityMetricsExtractor;

public class MetricsController {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  public MetricsController(String repository, String branch, Integer from, Integer to) {
    this.repository = repository;
    this.branch = branch;
    this.from = from;
    this.to = to;
  }

  public MetricsController(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
  }

  public Boolean extract() {
    QualityMetricsExtractor extractor = null;
    if(from != null && to != null) {
      extractor = new QualityMetricsExtractor(repository, branch, from, to);
    } else {
      extractor = new QualityMetricsExtractor(repository, branch);
    }
    extractor.extract();
    return true;
  }
  
}