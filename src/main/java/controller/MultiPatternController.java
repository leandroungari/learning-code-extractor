package controller;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.multipattern.MultiPatternProcessor;

public class MultiPatternController {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  public MultiPatternController(String repo, String branch, Integer from, Integer to) {
    this.repository = repo;
    this.branch = branch;
    this.from = from;
    this.to = to;
  }

  public void execute() throws GitAPIException {
    MultiPatternProcessor processor = null;
    if (from != null && to != null) {
      processor = new MultiPatternProcessor(repository, branch, from, to);
    } else {
      processor = new MultiPatternProcessor(repository, branch);
    }

    processor.identify();
  }
}