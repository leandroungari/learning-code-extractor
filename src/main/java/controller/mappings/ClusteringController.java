package controller.mappings;

import ast.ClusteringEngine;

public class ClusteringController {
  
  private String repository;
  private String branch;

  public ClusteringController(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
  }

  public Boolean clusterToStore() {
    var engine = new ClusteringEngine(repository, branch);
    engine.generate();
    return true;
  }
}