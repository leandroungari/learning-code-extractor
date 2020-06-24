package ast;

import ast.extractor.CommitMappings;
import ast.extractor.comparator.Comparator;
import spoon.support.SpoonClassNotFoundException;

public class MappingsExtractor {

  private String repository;
  private String branch;
  private String commit;

  public MappingsExtractor(String repository, String branch, String commit) {

    this.repository = repository;
    this.branch = branch;
    this.commit = commit;
  }

  public CommitMappings createMappings() {

    try {
      var comparator = new Comparator(repository, branch, commit);
      var items = comparator.generateMappings();

      var result = new CommitMappings(repository, branch, commit);
      result.add(items);

      return result;
    } catch(NullPointerException | SpoonClassNotFoundException ex) {
      return new CommitMappings(repository, branch, commit);
    }
  }

}