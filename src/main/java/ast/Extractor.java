package ast;

import ast.extractor.ModificationBuilder;

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

  public void transformModifications() {

    var builder = new ModificationBuilder(
      repository,
      branch,
      commit
    );

    builder.create();
  }


  


}