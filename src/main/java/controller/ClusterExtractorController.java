package controller;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.ClusterExtractor;

public class ClusterExtractorController {

  private String repository;
  private String branch;

  public ClusterExtractorController(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
  }

  public void execute() {

    try {
      var extractor = new ClusterExtractor(repository, branch);
      extractor.execute();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }
}