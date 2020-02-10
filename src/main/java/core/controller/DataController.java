package core.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.mauricioaniche.ck.CKClassResult;

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

  private ResourceTrack track = new ResourceTrack();

  private Resource<MetricsData[]> resource;

  public Map<String,MetricsData> metrics(String repository, Commit commit) {

    resource = track
      .<MetricsData[]>get(ResourceType.METRICS_FILES)
      .setParams(repository, commit.id().getName());

    if (!resource.storage().exists()) {
      analyzeCommit(repository, commit);
    }

    var result = resource.read(MetricsData[].class);
    var map = new HashMap<String,MetricsData>();

    for(var metric: result) {
      map.put(metric.getFile(), metric);
    }

    return map;
  }

  private void analyzeCommit(String repository, Commit commit) {
    
    var data = new ArrayList<MetricsData>();
    
    try {
      analyzer.repository(Repository.create(new File(
        settings.folderOfRepositories() + "/" +
        repository
      )))
      .enableLogs(false)
      .process(commit, (file, result) -> {
        var metrics = createMetrics(file, result);        
	      data.add(metrics);
      });
    }
    catch(GitRepositoryException e) {
      e.printStackTrace();
    }
  
    resource.write(data.toArray(new MetricsData[data.size()]));
  }

  private MetricsData createMetrics(String file, CKClassResult result) {

    var metrics = new MetricsData();
		metrics.setFile(file);
		metrics.setCbo((double) result.getCbo());
		metrics.setDit((double) result.getDit());
		metrics.setLcom((double) result.getLcom());
		metrics.setLoc((double) result.getLoc());
		metrics.setNosi((double) result.getNosi());
		metrics.setNumberOfFields((double) result.getNumberOfFields());
		metrics.setNumberOfMethods((double) result.getNumberOfMethods());
		metrics.setRfc((double) result.getRfc());
    metrics.setWmc((double) result.getWmc());
    
    return metrics;
  }
}