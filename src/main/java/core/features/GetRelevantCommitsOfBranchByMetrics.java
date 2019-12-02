package core.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.controller.DataController;
import core.controller.StatsController;
import repository.Commit;

public class GetRelevantCommitsOfBranchByMetrics implements RepositoryFeature<Map<String, List<Map<String, Double>>>> {

  public static final String SUM_OPERATION = "sum";
  public static final String AVERAGE_OPERATION = "average";

  private String repository;
  private String branch;
  private String metric;
  private String type;

  private DataController data = new DataController();
  private StatsController stats = new StatsController();

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

    var data = new HashMap<String, List<Map<String, Double>>>();
    var derivateOfBranch = calculateDerivateOfBranch();
    var derivateOfCommits = calculateDerivateOfAllCommits();

    data.put("good", identifyGoodCommits(derivateOfBranch, derivateOfCommits));
    data.put("bad", identifyBadCommits(derivateOfBranch, derivateOfCommits));

    return data;
  }

  private List<Map<String,Double>> identifyBadCommits (
    Map<String, Double> average, 
    Map<String, Map<String, Double>> derivates) {

    var bad = new ArrayList<Map<String, Double>>();
    derivates.forEach((commit, metrics) -> {
      if (metrics.get(metric) > average.get(metric)) {
        bad.add(Map.<String, Double>of(commit, metrics.get(metric)));
      }
    });

    bad.sort((a, b) -> {
      var firstValue = a.values().toArray(new Double[1])[0];
      var secondValue = b.values().toArray(new Double[1])[0];

      if (firstValue < secondValue)
        return 1;
      else if (firstValue > secondValue)
        return -1;
      else
        return 0;
    });

    return bad;
  } 

  private List<Map<String, Double>> identifyGoodCommits (
    Map<String, Double> average, 
    Map<String, Map<String, Double>> derivates) {

    var good = new ArrayList<Map<String, Double>>();
    derivates.forEach((commit, metrics) -> {
      if (metrics.get(metric) < average.get(metric)) {
        good.add(Map.<String, Double>of(commit, metrics.get(metric)));
      }
    });

    good.sort((a, b) -> {
      var firstValue = a.values().toArray(new Double[1])[0];
      var secondValue = b.values().toArray(new Double[1])[0];

      if (firstValue < secondValue)
        return -1;
      else if (firstValue > secondValue)
        return 1;
      else
        return 0;
    });

    return good;
  }

  private Map<String, Map<String, Double>> calculateDerivateOfAllCommits() {

    try {
      var repo = createRepository(repository);
      var commits = repo.branch(branch).commits();

      var result = new HashMap<String,Map<String,Double>>();
      for(var i = 0; i < commits.size() - 1; i++) {
        result.put(
          commits.get(i+1).id().name(),
          derivateMetricsFromCommits(commits.get(i), commits.get(i+1))
        );
      }

      return result;
    
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    return null;
  }

  private Map<String, Double> calculateDerivateOfBranch() {

    try {

      var repo = createRepository(repository);
      var commits = repo.branch(branch).commits();

      var firstCommit = commits.get(0);
      var lastCommit = commits.get(commits.size() - 1);

      return derivateMetricsFromCommits(firstCommit, lastCommit);
    
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    return null;
  }

  private Map<String, Double> derivateMetricsFromCommits(Commit firstCommit, Commit lastCommit) {
    
    Map<String, Double> metricsFromFirstCommit, metricsFromLastCommit;

    if(SUM_OPERATION.equals(type)) {
      metricsFromFirstCommit = calculateMetricsFromCommitBySum(firstCommit);
      metricsFromLastCommit = calculateMetricsFromCommitBySum(lastCommit);
    }
    else {
      metricsFromFirstCommit = calculateMetricsFromCommitByAverage(firstCommit);
      metricsFromLastCommit = calculateMetricsFromCommitByAverage(lastCommit);
    }

    return derivateValues(metricsFromFirstCommit, metricsFromLastCommit);
  }

  private Map<String,Double> calculateMetricsFromCommitByAverage(Commit commit) {
    
    var metrics = data.metrics(repository, commit.id().name());
    return stats.averageOfMetrics(metrics);
  }

  private Map<String,Double> calculateMetricsFromCommitBySum(Commit commit) {
    
    var metrics = data.metrics(repository, commit.id().name());
    return stats.sumOfMetrics(metrics);
  }

  private Map<String,Double> derivateValues(Map<String,Double> a, Map<String,Double> b) {

    var result = new HashMap<String,Double>();
  
    result.put("cbo", ((b.get("cbo") - a.get("cbo"))/2));
    result.put("dit", ((b.get("dit") - a.get("dit"))/2));
    result.put("nosi", ((b.get("nosi") - a.get("nosi"))/2));
    result.put("rfc", ((b.get("rfc") - a.get("rfc"))/2));
    result.put("wmc", ((b.get("wmc") - a.get("wmc"))/2));
  
    return result;
  }

}