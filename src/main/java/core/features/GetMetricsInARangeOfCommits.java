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
  private Integer step;

  @Override
  public Feature<Map<String,Map<String,Map<String, Double>>>> setParams(String... params) {

    repository = params[0];
    branch = params[1];
    from = Integer.valueOf(params[2]);
    to = Integer.valueOf(params[3]);
    step = (params[4] != null ? Integer.valueOf(params[4]) : -1);

    return this;
  }

  @Override
  public Map<String,Map<String,Map<String, Double>>> action() {

    var commits = getCommits();
    return getMetricsOfCommits(commits);
  }

  private List<Commit> getCommits() {
    
    try {
      var repo = createRepository(repository);
      if(step == -1) {
        return repo.branch(branch).commits(from, to);
      }
      else {
        return repo.branch(branch).commits(from, to, step);
      }
    } 
    catch (GitAPIException e) {
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