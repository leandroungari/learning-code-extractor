package core.features;

import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

public class GetDiffInCommit implements RepositoryFeature<List<Map<String, String>>> {

  private String repository;
  private String branch;
  private String commit;

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

      var repo = createRepository(repository);
      var ref = repo.branch(branch).commit(commit);
      return ref.showDiff();

    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    return null;
  }
}