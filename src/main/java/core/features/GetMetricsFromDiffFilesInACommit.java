package core.features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.controller.DataController;
import core.data.DiffFile;
import core.data.MetricsData;

public class GetMetricsFromDiffFilesInACommit 
  implements RepositoryFeature<Map<String,MetricsData>> {

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
  public Map<String,MetricsData> action() {

    var repo = createRepository(repository);
    var result = new HashMap<String, MetricsData>();

    try {
      var metrics = data.metrics(
        repository, 
        repo.branch(branch).commit(commit)
      );

      var modifiedFiles = getModifiedFilesFromCommit();

      metrics.forEach((file, metric) -> {
        if(modifiedFiles.contains(metric.getFile())) {
          result.put(metric.getFile(), metric);
        }
      });

    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    return result;
  }

  public List<String> getModifiedFilesFromCommit() {

    var list = player
      .<List<DiffFile>>get(FeatureType.DIFF_FILES_IN_COMMIT)
      .setParams(repository,branch,commit)
      .action();

    return list
      .stream()
      .map(DiffFile::getNewPath)
      .collect(Collectors.toList());
  }
}