package core.features;

import java.util.Map;

import core.controller.DataController;

public class GetMetricsInACommit 
  implements RepositoryFeature<Map<String, Map<String, Double>>> {

  private DataController data = new DataController();

  private String repository;
  private String commit;

  @Override
  public Feature<Map<String, Map<String, Double>>> setParams(String... params) {

    repository = params[0];
    commit = params[1];
    return this;
  }

  @Override
  public Map<String, Map<String, Double>> action() {
    
    return data.metrics(repository, commit);
  }
}