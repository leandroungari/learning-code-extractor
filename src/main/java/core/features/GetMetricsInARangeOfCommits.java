package core.features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import repository.Commit;

public class GetMetricsInARangeOfCommits 
  implements RepositoryFeature<Map<String,Map<String,Map<String, Double>>>> {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  @Override
  public Feature<Map<String,Map<String,Map<String, Double>>>> setParams(String... params) {

    repository = params[0];
    branch = params[1];
    from = Integer.valueOf(params[2]);
    to = Integer.valueOf(params[3]);

    return this;
  }

  @Override
  public Map<String,Map<String,Map<String, Double>>> action() {

    try {
      
      var repo = createRepository(repository);
      var commits = repo.branch(branch).commits(from, to);
      return getMetricsOfCommits(commits);
      
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    return null;
  }

  private HashMap<String,Map<String, Map<String, Double>>> getMetricsOfCommits(List<Commit> commits) {
    
    var map = new HashMap<String,Map<String,Map<String,Double>>>();

    for (var commit : commits) {
      var result = data.metrics(repository, commit.id().name());
      map.put(commit.id().name(), result);
    }

    return map;
  }

}