package controller;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.ClusterExtractor;
import util.Timer;

public class ClusterExtractorController {

  private String repository;
  private String branch;

  public ClusterExtractorController(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
  }

  public void execute() {

    var timer = new Timer();
    try {
      var extractor = new ClusterExtractor(repository, branch);
      timer.start();
      extractor.execute();
      timer.finish();
      timer.show();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }
}