package api.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import core.Player;
import core.data.Commit;
import core.data.MetricsData;
import repository.exception.GitRepositoryException;

@RestController
public class Plots {

  private static Player player = new Player();

  private Function<HashMap<String,Double[]>,Double[]> 
    averageOfMetricsOfFiles = files -> {
    
    var sum = sumOfMetricsOfFiles(files);
    var size = files.size();
    return new Double[]{
      sum[0]/size,
      sum[1]/size,
      sum[2]/size,
      sum[3]/size,
      sum[4]/size,
    };
  };

  @GetMapping("/plots/averageMetricsFiles/{repository}/{branch}/{from}/{to}")
  @ResponseBody
  public Map<String,Map<String,Double>> averageMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to)
      throws GitRepositoryException, 
             GitAPIException {

    var data = player
      .metricsFromDiffFilesInARangeOfCommits(
        repository, 
        branch, 
        extractRangeOfCommits(repository, branch, from, to)
      );

    return extractValuesFromCommits(
      data, 
      averageOfMetricsOfFiles
    );
  }

  private Commit[] extractRangeOfCommits(
    String repository, 
    String branch, 
    Integer from, 
    Integer to) throws GitAPIException, 
                       GitRepositoryException {
    
    var allCommits = player.commits(repository, branch);
    var listOfCommits = new ArrayList<Commit>();
    for(var i = from; i <= to; i++) {
      listOfCommits.add(allCommits[i]);
    }
    return listOfCommits
      .toArray(new Commit[listOfCommits.size()]);
  }

  private Map<String,Map<String,Double>> extractValuesFromCommits(
    Map<String,MetricsData> data,
    Function<HashMap<String,Double[]>,Double[]> action) {

    var result = new HashMap<String,Map<String,Double>>();
    data.forEach((commit, files) -> {
      var metrics = action.apply(files.getFiles());
      result.put(commit, extractValuesFromCommit(metrics));
    });

    return result;
  }

  private Map<String,Double> extractValuesFromCommit(Double[] data) {
    return Map.ofEntries(
      Map.entry("cbo", data[0]),
      Map.entry("dit", data[1]),
      Map.entry("nosi", data[2]),
      Map.entry("rfc", data[3]),
      Map.entry("wmc", data[4])
    );
  }

  private Double[] sumOfMetricsOfFiles(HashMap<String,Double[]> files) {
    var sumOfMetrics = new Double[]{
      0.0,0.0,0.0,0.0,0.0
    };

    files.forEach((file,metrics) -> {
      sumOfMetrics[0] += metrics[0];
      sumOfMetrics[1] += metrics[1];
      sumOfMetrics[2] += metrics[2];
      sumOfMetrics[3] += metrics[3];
      sumOfMetrics[4] += metrics[4];
    });

    return sumOfMetrics;
  }

}
