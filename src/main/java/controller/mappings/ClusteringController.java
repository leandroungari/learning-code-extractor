package controller.mappings;

import ast.ClusteringEngine;

public class ClusteringController {
  
  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  public ClusteringController(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
  }

  public ClusteringController(String repository, String branch, Integer from, Integer to) {
    this.repository = repository;
    this.branch = branch;
    this.from = from;
    this.to = to;
  }

  public Boolean clusterToStore() {
    if(from != null && to != null) {
      var engine = new ClusteringEngine(repository, branch, from, to);
      engine.generate();
    } else {
      var engine = new ClusteringEngine(repository, branch);
      engine.generate();
    }
    return true;
  }
}