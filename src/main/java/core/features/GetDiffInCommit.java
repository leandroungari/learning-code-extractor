package core.features;

import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.features.Feature;
import repository.exception.GitRepositoryException;

public class GetDiffInCommit extends Feature<List<Map<String, String>>> {

  private String repository;
  private String branch;
  private String commit;

  public GetDiffInCommit() {
    super();
  }

  @Override
  public Feature<List<Map<String, String>>> setParams(String... params) {

    repository = params[0];
    branch = params[1];
    commit = params[2];

    return this;
  }

  @Override
  public List<Map<String, String>> action() {

    try {
      return player
        .repository(repository)
        .diffInCommit(branch, commit);

    } catch (GitRepositoryException | GitAPIException e) {
      e.printStackTrace();
    }

    return null;
  }
}