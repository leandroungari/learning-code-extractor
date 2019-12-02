package core.features;

import core.data.MetricsData;

public class GetMetricsInACommit 
  implements RepositoryFeature<MetricsData> {

  private String repository;
  private String commit;

  @Override
  public Feature<MetricsData> setParams(String... params) {

    repository = params[0];
    commit = params[1];
    return this;
  }

  @Override
  public MetricsData action() {
    
    return data.metrics(repository, commit);
  }
}