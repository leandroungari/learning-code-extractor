package controller.mappings;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.ClusteringEngine;
import ast.snapshot.Snapshot;

public class ClusteringController {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  public ClusteringController(String repository, String branch) throws GitAPIException {
    this.repository = repository;
    this.branch = branch;

    Snapshot.create(repository, branch);
  }

  public ClusteringController(String repository, String branch, Integer from, Integer to) throws GitAPIException {
    this.repository = repository;
    this.branch = branch;
    this.from = from;
    this.to = to;

    Snapshot.create(repository, branch);
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