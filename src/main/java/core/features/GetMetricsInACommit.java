package core.features;

import java.io.File;

import core.data.MetricsData;
import core.resource.Resource;
import core.resource.ResourceType;
import repository.Repository;
import repository.exception.GitRepositoryException;
import repository.metrics.MetricsAnalyzer;

public class GetMetricsInACommit extends Feature<MetricsData> {

  private String repository;
  private String commit;

  private MetricsAnalyzer analyzer;

  public GetMetricsInACommit() {
    super();
    analyzer = new MetricsAnalyzer();
  }

  @Override
  public Feature<MetricsData> setParams(String... params) {

    repository = params[0];
    commit = params[1];
    return this;
  }

  @Override
  public MetricsData action() {
    var resourceAll = Resource
      .create(repository, ResourceType.METRICS_ALL_FILES)
      .folder("commits")
      .name(commit);

    if (!resourceAll.exists()) {
      createResource();
    }

    return resourceAll.read(MetricsData.class);
  }

  private void createResource() {

    try {
      
      var data = new MetricsData();
    
      Repository.create(
        new File(player.settings().folderOfRepositories() 
        + "/" + repository)
      );

      analyzer
      .repositoryName(repository)
      .enableLogs(false)
      .process(commit, (file, result) -> {
        data.addFiles(file, result);
      });

      Resource
      .create(repository, ResourceType.METRICS_ALL_FILES)
      .folder("commits")
      .name(commit)
      .write(data);
    
    } catch (GitRepositoryException e) {
      e.printStackTrace();
    }
  }
}