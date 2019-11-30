package core.features;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.data.MetricsData;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class GetMetricsInARangeOfCommits extends Feature<Map<String, MetricsData>> {

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

    var map = new HashMap<String, MetricsData>();

    try {
      
      var repo = Repository.create(new File(
        player.settings().folderOfRepositories() 
        + "/" + repository
      ));

      var commits = repo.branch(branch).commits(from, to);

      for (var commit : commits) {
        var result = player
          .<MetricsData>get(FeatureType.METRICS_IN_COMMIT)
          .setParams(repository, commit.id().name())
          .action();

        map.put(commit.id().name(), result);
      }
    } catch (GitRepositoryException | GitAPIException e) {
      e.printStackTrace();
    }

    return map;
  }

}