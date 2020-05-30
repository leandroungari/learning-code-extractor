package ast;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.extractor.CommitMappings;
import ast.extractor.comparator.Comparator;
import repository.Repository;

public class MappingsExtractor {

  private String repository;
  private String branch;
  private String commit;

  private Repository instance;

  public MappingsExtractor(String repository, String branch, String commit) {

    this.repository = repository;
    this.branch = branch;
    this.commit = commit;

    this.instance = Repository.create(repository);
  }

  public CommitMappings createMappings() {

    try {
      var comparator = new Comparator(
        instance.branch(branch).commit(commit)
      );
      var items = comparator.generateMappings();

      var result = new CommitMappings(repository, branch, commit);
      result.add(items);

      return result;
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    return null;
  }

}