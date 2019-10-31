package api.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    sumOfMetricsOfFiles = files -> {

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
  };

  private Function<HashMap<String,Double[]>,Double[]> 
    averageOfMetricsOfFiles = files -> {
    
    var sum = sumOfMetricsOfFiles.apply(files);
    var size = files.size();
    return new Double[]{
      sum[0]/size,
      sum[1]/size,
      sum[2]/size,
      sum[3]/size,
      sum[4]/size,
    };
  };

  @GetMapping("/plots/averageMetricsFiles/{repository}/{branch}/{from}/{to}/{step}")
  @ResponseBody
  public Map<String,Map<String,Double>> averageMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to,
    @PathVariable Integer step)
      throws GitRepositoryException, 
             GitAPIException {

    return extractValuesFromCommits(
      data(repository,branch,from,to,step),
      averageOfMetricsOfFiles
    );
  }

  @GetMapping("/plots/sumMetricsFiles/{repository}/{branch}/{from}/{to}/{step}")
  @ResponseBody
  public Map<String,Map<String,Double>> sumMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to,
    @PathVariable Integer step)
      throws GitRepositoryException, 
             GitAPIException {

    return extractValuesFromCommits(
      data(repository,branch,from,to,step),
      averageOfMetricsOfFiles
    );
  }

  @GetMapping("/plots/sumMetricsFiles/{repository}/{branch}/{from}/{to}/{step}/normalized")
  @ResponseBody
  public Map<String,Map<String,Double>> normalizedSumMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to,
    @PathVariable Integer step)
      throws GitRepositoryException, 
             GitAPIException {


              //arrumar tudo isso
    var result = sumMetricsFiles(repository, branch, from, to, step);
    Double[][] normalizedData = normalizeMetrics(new Double[][]{
      (Double[]) result.values().stream().map(a -> a.get("cbo")).toArray(),
      (Double[]) result.values().stream().map(a -> a.get("dit")).toArray(),
      (Double[]) result.values().stream().map(a -> a.get("nosi")).toArray(),
      (Double[]) result.values().stream().map(a -> a.get("rfc")).toArray(),
      (Double[]) result.values().stream().map(a -> a.get("wmc")).toArray()
    });


    Map<String,Map<String,Double>> data = Map.of();
    var commits = result.
    for(var i = 0; i < commits.size(); i++) {
      commits.
    }
  }

  private HashMap<String,MetricsData> data(
    String repository, 
    String branch, 
    Integer from, 
    Integer to,
    Integer step) throws GitRepositoryException, 
                       GitAPIException {
      
    var listOfCommits = extractRangeOfCommits(
      repository,branch,from,to,step
    );

    return (
      step == 1 ? 
      diffFiles(repository, branch, listOfCommits) :
      allFilesData(repository, branch, listOfCommits)
    );
  }

  private HashMap<String,MetricsData> allFilesData(
    String repository,
    String branch,
    Commit[] listOfCommits
  ) throws GitRepositoryException, GitAPIException {

    return player
      .metricsFromAllFilesInARangeOfCommits(
        repository,branch,listOfCommits
      );
  }

  private HashMap<String,MetricsData> diffFiles(
    String repository,
    String branch,
    Commit[] listOfCommits
  ) throws GitRepositoryException, GitAPIException {
    
    var total = player
      .metricsFromDiffFilesInARangeOfCommits(
        repository,branch,listOfCommits
      );

    var firstCommit = listOfCommits[listOfCommits.length - 1]
      .getId().getName();

    total.put(
      firstCommit,
      player.metricsFromAllFilesInACommit(
        repository,branch,firstCommit
      )
    );

    return total;
  }

  private Commit[] extractRangeOfCommits(
    String repository, 
    String branch, 
    Integer from, 
    Integer to,
    Integer step) 
      throws GitAPIException, 
             GitRepositoryException {
    
    var allCommits = player.commits(repository, branch);
    var listOfCommits = new ArrayList<Commit>();
    for(var i = from; i <= to; i+= step) {
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

  private Double[] normalize(Double[] list) {

    var data = Arrays.stream(list);
    var min = data.min(Double::compare).get();
    var max = data.max(Double::compare).get();

    return (Double[]) data
      .map(num -> (num - min) / (max - min))
      .toArray();
  }

  private Double[][] normalizeMetrics(Double[][] lists) {
    
    return new Double[][]{
      normalize(lists[0]),
      normalize(lists[1]),
      normalize(lists[2]),
      normalize(lists[3]),
      normalize(lists[4])
    };
  }

  

}
