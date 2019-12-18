package core.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import core.data.MetricsData;
import core.resource.Resource;
import core.resource.ResourceTrack;
import core.resource.ResourceType;
import repository.Commit;
import repository.Repository;
import repository.exception.GitRepositoryException;
import repository.metrics.MetricsAnalyzer;

public class DataController {

  private static final MetricsAnalyzer analyzer = new MetricsAnalyzer();
  private static final SettingsController settings = SettingsController.get();

  private static final Integer CBO = 0;
  private static final Integer DIT = 1;
  private static final Integer NOSI = 4;
  private static final Integer RFC = 5;
  private static final Integer WMC = 6;

  private ResourceTrack track = new ResourceTrack();

  private Resource resource;

  public Map<String, Map<String, Double>> metrics(String repository, Commit commit) {

    resource = track
      .get(ResourceType.METRICS_FILES)
      .setParams(repository, commit.id().getName());

    if (!resource.exists()) {
      analyzeCommit(repository, commit);
    }

    var metrics = resource.read(MetricsData.class);
    return convertFileMetricsToMap(metrics);
  }

  private HashMap<String, Map<String, Double>> convertFileMetricsToMap(MetricsData metrics) {
    
    var result = new HashMap<String,Map<String,Double>>();

    metrics.getFiles().forEach((file,data) -> {
      result.put(
        file,
        Map.of(
          "cbo", data[CBO],
          "dit", data[DIT],
          "nosi", data[NOSI],
          "rfc", data[RFC],
          "wmc", data[WMC]
        )
      );
    });

    return result;
  }

  private void analyzeCommit(String repository, Commit commit) {
    
    var data = new MetricsData();
    
    try {
      analyzer
      .repository(Repository.create(new File(
        settings.folderOfRepositories() + "/" +
        repository
      )))
      .enableLogs(false)
      .process(commit, (file, result) -> {
        data.addFiles(file, result);
      });
    }
    catch(GitRepositoryException e) {
      e.printStackTrace();
    }
    

    resource.write(data);
  }
}