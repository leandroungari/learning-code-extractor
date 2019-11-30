package core.features;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.data.Branch;
import repository.Repository;

public class GetRelevantCommitsOfBranchByMetrics extends Feature<Map<String, List<Map<String, Double>>>> {

  private static final String SUM_OPERATION = "sum";
  private static final String AVERAGE_OPERATION = "average";

  private String repository;
  private String branch;
  private String metric;
  private String type;

  public GetRelevantCommitsOfBranchByMetrics() {
    super();
  }

  @Override
  public Feature<Map<String, List<Map<String, Double>>>> setParams(String... params) {
    
    repository = params[0];
    branch = params[1];
    metric = params[2];
    type = params[3];

    return this;
  }

  @Override
  public Map<String, List<Map<String, Double>>> action() {
    
    var data = new HashMap<String, List<Map<String,Double>>>();

    var average = derivateRepository(repository, type)
      .get(branch);
      
    var branchData = derivateBranch(repository, branchId, type);
  
    var good = new ArrayList<Map<String,Double>>();
    branchData.forEach((commit,metrics) -> {
      if(metrics.get(metric) < average.get(metric)) {
        good.add(Map.<String,Double>of(commit,metrics.get(metric)));
      }
    });

    good.sort((a,b) -> {
      var firstValue = a.values().toArray(new Double[1])[0];
      var secondValue = b.values().toArray(new Double[1])[0];

      if(firstValue < secondValue) return -1;
      else if(firstValue > secondValue) return 1;
      else return 0;
    });

    var bad = new ArrayList<Map<String,Double>>();
    branchData.forEach((commit,metrics) -> {
      if(metrics.get(metric) > average.get(metric)) {
        bad.add(Map.<String,Double>of(commit,metrics.get(metric)));
      }
    });

    bad.sort((a,b) -> {
      var firstValue = a.values().toArray(new Double[1])[0];
      var secondValue = b.values().toArray(new Double[1])[0];

      if(firstValue < secondValue) return 1;
      else if(firstValue > secondValue) return -1;
      else return 0;
    });

    data.put("good", good);
    data.put("bad", bad);

    return data;
  }

  private Map<String, Map<String, Double>> calculateAverageOfBranch() {

    Map<String, Map<String, Double>> result = new HashMap<>();
    
    var branches = player
      .<Branch[]>get(FeatureType.BRANCHES_OF_REPOSITORY)
      .setParams(repository)
      .action();

    for (var branch : branches) {
      var name = branch.getId().getName();
      result.put(name, execute(name, type));
    }

    return result;
  }

  private Map<String,Double> calculateDerivateFromCommits() {

    var repo = Repository.create(new File(
      player.settings().folderOfRepositories()
      + "/" + repository
    ));

    var firstCommit = 

    var metricsStart = (
      SUM_OPTION.equals(type) ?
      getSumOfMetricsOfFiles() : 
      getAverageOfMetricsOfFiles() 
    )
    .apply(
      player
      .metrics(repository)
      .metricsFromAllFilesInACommit(branch, commitBegin)
      .getFiles()
    );
    
    var metricsEnd = (
      SUM_OPTION.equals(type) ?
      getSumOfMetricsOfFiles() : 
      getAverageOfMetricsOfFiles() 
    )
    .apply(
      player
      .metrics(repository)
      .metricsFromAllFilesInACommit(branch, commitEnd)
      .getFiles()
    );
    
    return derivateMetrics(metricsStart, metricsEnd, size);
  }

}