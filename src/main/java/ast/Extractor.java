package ast;

import java.util.List;

import ast.extractor.Changes;
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

  public CommitChanges transformModifications() {

    var builder = new ModificationBuilder(
      repository,
      branch,
      commit
    );

    return builder.create();
  }


  


}