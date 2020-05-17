package ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.extractor.CommitModifications;
import ast.extractor.comparator.Comparator;
import ast.extractor.mapping.Mapping;
import repository.Branch;
import repository.Commit;
import repository.Repository;

public class Extractor {

  private String repository;
  private String branch;
  private String commit;

  public Extractor(String repository, String branch, String commit) {
    this.repository = repository;
    this.branch = branch;
    this.commit = commit;
  }

  public Extractor(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
  }

  private List<Commit> getListOfCommits() {

    Branch instance;
    try {
      instance = Repository.create(repository).branch(branch);
      if(commit != null) {
        return List.of(instance.commit(commit));
      } else {
        var result = instance.commits();
        return result.subList(0, result.size() - 1);
      }
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    throw new Error("Inexisting branch or commit at repository");
  }

  public List<Mapping> extract() {
    var result = new ArrayList<Mapping>();

    var commits = this.getListOfCommits();
    for(var commit: commits) {
      var comparator = new Comparator(commit);
      result.addAll(comparator.generateMappings());
    }

    return result;
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