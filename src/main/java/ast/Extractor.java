package ast;

import ast.extractor.CommitModifications;

public class Extractor {

  private String repository;
  private String branch;
  private String commit;

  public Extractor(
    String repository, 
    String branch, 
    String commit) {

    this.repository = repository;
    this.branch = branch;
    this.commit = commit;
  }

  public CommitModifications transformModifications() {

    var builder = new ModificationsBuilder(
      repository,
      branch,
      commit
    );

    return builder.createModifications();
  }

}