package core.features;

import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.controller.DataController;
import core.data.MetricsData;

public class GetMetricsInACommit 
  implements RepositoryFeature<Map<String, MetricsData>> {

  private DataController data = new DataController();

  private String repository;
  private String branch;
  private String commit;

  @Override
  public Feature<Map<String, MetricsData>> setParams(String... params) {

    repository = params[0];
    branch = params[1];
    commit = params[2];
    return this;
  }

  @Override
  public Map<String, MetricsData> action() {

    var repo = createRepository(repository);
    try {

      return data.metrics(
        repository, 
        repo.branch(branch).commit(commit)
      );

    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    return null;
  }
}