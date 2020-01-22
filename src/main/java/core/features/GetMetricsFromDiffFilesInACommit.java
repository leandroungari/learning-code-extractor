package core.features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.controller.DataController;
import core.data.DiffFile;

public class GetMetricsFromDiffFilesInACommit implements RepositoryFeature<Map<String, Map<String, Double>>> {

  private DataController data = new DataController();

  private String repository;
  private String branch;
  private String commit;

  @Override
  public Feature<Map<String, Map<String, Double>>> setParams(String... params) {

    repository = params[0];
    branch = params[1];
    commit = params[2];
    return this;
  }

  @Override
  public Map<String, Map<String, Double>> action() {

    var repo = createRepository(repository);
    var result = new HashMap<String, Map<String,Double>>();

    try {
      var metrics = data.metrics(repository, repo.branch(branch).commit(commit));

      var modifiedFiles = getModifiedFilesFromCommit();

      metrics.forEach((file,data) -> {
        
        if(modifiedFiles.contains(file)) result.put(file, data);
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