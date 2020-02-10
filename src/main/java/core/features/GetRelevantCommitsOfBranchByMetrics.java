package core.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.controller.DataController;
import core.controller.StatsController;
import core.data.MetricsData;
import repository.Commit;

public class GetRelevantCommitsOfBranchByMetrics 
  implements RepositoryFeature<Map<String,List<Map<String, Double>>>> {

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
    var derivateOfCommits = calculateDerivateOfAllCommits();
    var ordered = sort(derivateOfCommits);

    data.put("good", identifyGoodCommits(ordered));
    data.put("bad", identifyBadCommits(ordered));

    return data;
  }

  private List<MetricsData> sort(Map<String, MetricsData> values) {

    var list = new ArrayList<MetricsData>();
    values.forEach((id, metrics) -> {
      list.add(Map.<String,Double>of(id,metrics.get(metric)));
    });

    list.sort((a,b) -> {
      if(getValue(a) < getValue(b)) return -1;
      if(getValue(a) > getValue(b)) return 1;
      return 0;
    });

    return list;
  }

  private Double getValue(Map<String,Double> value) {
    return value.values().toArray(new Double[1])[0];
  }

  private List<MetricsData> identifyBadCommits(List<MetricsData> list) {

    var bad = new ArrayList<MetricsData>();
    for(var i = Math.round(list.size()/2); i < list.size(); i++) {
      bad.add(0, list.get(i));
    }

    return bad;
  } 

  private List<MetricsData> identifyGoodCommits (List<MetricsData> list) {

    var good = new ArrayList<MetricsData>();
    for(var i = 0; i < Math.round(list.size()/2); i++) {
      good.add(list.get(i));
    }

    return good;
  }

  private Map<String, MetricsData> calculateDerivateOfAllCommits() {

    try {
      var repo = createRepository(repository);
      var commits = repo.branch(branch).commits();

      var result = new HashMap<String,MetricsData>();
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

  private MetricsData derivateMetricsFromCommits(Commit firstCommit, Commit lastCommit) {
    
    MetricsData metricsFromFirstCommit, metricsFromLastCommit;

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

  private MetricsData calculateMetricsFromCommitByAverage(Commit commit) {
    
    var metrics = data.metrics(repository, commit);
    return stats.averageOfMetrics(metrics);
  }

  private MetricsData calculateMetricsFromCommitBySum(Commit commit) {
    
    var metrics = data.metrics(repository, commit);
    return stats.sumOfMetrics(metrics);
  }

  private MetricsData derivateValues(MetricsData a, MetricsData b) {

    var result = new MetricsData();
    result.setCbo((b.getCbo() - a.getCbo())/2);
    result.setDit((b.getDit() - a.getDit())/2);
    result.setRfc((b.getRfc() - a.getRfc())/2);
    result.setWmc((b.getWmc() - a.getWmc())/2);
    result.setNosi((b.getNosi() - a.getNosi())/2);

    return result;
  }

}