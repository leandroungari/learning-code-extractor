package core.features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.data.MetricsData;
import repository.Commit;

public class GetMetricsInARangeOfCommits 
  implements RepositoryFeature<Map<String, MetricsData>> {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  @Override
  public Feature<Map<String, MetricsData>> setParams(String... params) {

    repository = params[0];
    branch = params[1];
    from = Integer.valueOf(params[2]);
    to = Integer.valueOf(params[3]);

    return this;
  }

  @Override
  public Map<String, MetricsData> action() {

    try {
      
      var repo = createRepository(repository);
      var commits = repo.branch(branch).commits(from, to);
      return getMetricsOfCommits(commits);

      
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    return null;
  }

  private Map<String, MetricsData> getMetricsOfCommits(List<Commit> commits) {
    
    var map = new HashMap<String, MetricsData>();

    for (var commit : commits) {
      var result = data.metrics(repository, commit.id().name());
      map.put(commit.id().name(), result);
    }

    return map;
  }

}