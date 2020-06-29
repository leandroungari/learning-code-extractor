package controller;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.findings.Finder;

public class FinderController {

  private String repositoryA;
  private String branchA;
  private String repositoryB;
  private String branchB;

  public FinderController(String repositoryA, String branchA, String repositoryB, String branchB) {

    this.repositoryA = repositoryA;
    this.branchA = branchA;
    this.repositoryB = repositoryB;
    this.branchB = branchB;
  }

  public void execute() throws GitAPIException {
    var finder = new Finder(repositoryA, branchA);
    finder.setRepositoryTarget(repositoryB, branchB);
    var data = finder.compatibility();
    finder.toStore(data);
  }
}