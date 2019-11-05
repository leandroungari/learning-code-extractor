package api.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import core.Player;
import core.data.Commit;
import core.data.DiffFile;
import core.data.MetricsData;
import core.metricsexecutor.Stats;
import repository.exception.GitRepositoryException;

@RestController
public class Plots {

  private static Player player = new Player();

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
      Stats.getAverageOfMetricsOfFiles()
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
      Stats.getSumOfMetricsOfFiles()
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

    var data = sumMetricsFiles(repository, branch, from, to, step);
    return normalizeMetrics(data);
  }

  @GetMapping("/plots/averageMetricsFiles/{repository}/{branch}/{from}/{to}/{step}/normalized")
  @ResponseBody
  public Map<String,Map<String,Double>> normalizedAverageMetricsFiles(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to,
    @PathVariable Integer step)
      throws GitRepositoryException, 
             GitAPIException {

    var data = averageMetricsFiles(repository, branch, from, to, step);
    return normalizeMetrics(data);
  }

  @GetMapping("/plots/evolution/{repository}/{branch}/{from}/{to}/{step}/{metric}")
  @ResponseBody
  public Map<String,Map<String,Double>> evolutionOfFilesByMetrics(
    @PathVariable String repository, 
    @PathVariable String branch, 
    @PathVariable Integer from, 
    @PathVariable Integer to,
    @PathVariable Integer step,
    @PathVariable String metric)
      throws GitRepositoryException, 
             GitAPIException {

    var data = data(repository,branch,from,to,step);
    
    var result = new HashMap<String,Map<String,Double>>();
    data.forEach((commit, files) -> {
      result.put(
        commit,
        Stats.getEvolution().apply(files.getFiles(), metric)
      );
    });

    return result;
  }

  private Map<String,Map<String,Double>> normalizeMetrics(Map<String,Map<String,Double>> data) {
    
    Map<String,Double[]> stats = extractStats(data);
    var cbo = stats.get("cbo");
    var dit = stats.get("dit");
    var nosi = stats.get("nosi");
    var rfc = stats.get("rfc");
    var wmc = stats.get("wmc");

    var result = new HashMap<String,Map<String,Double>>();
    data.forEach((commit, metrics) -> {

      result.put(commit, Map.ofEntries(
        Map.entry("cbo", (metrics.get("cbo")-cbo[0])/(cbo[1]-cbo[0] != 0 ? cbo[1]-cbo[0] : 1)),
        Map.entry("dit", (metrics.get("dit")-dit[0])/(dit[1]-dit[0] != 0 ? dit[1]-dit[0] : 1)),
        Map.entry("nosi", (metrics.get("nosi")-nosi[0])/(nosi[1]-nosi[0] != 0 ? nosi[1]-nosi[0] : 1)),
        Map.entry("rfc", (metrics.get("rfc")-rfc[0])/(rfc[1]-rfc[0] != 0 ? rfc[1]-rfc[0] : 1)),
        Map.entry("wmc", (metrics.get("wmc")-wmc[0])/(wmc[1]-wmc[0] != 0 ? wmc[1]-wmc[0] : 1))
      ));
    });

    return result;
  }

  private Map<String,Double[]> extractStats(Map<String,Map<String,Double>> result) {
    return Map.ofEntries(
      Map.entry(
        "cbo", 
        normalize(
          result.values().stream()
            .map(a -> a.get("cbo"))
            .collect(Collectors.toList())
        )
      ),
      Map.entry(
        "dit",
        normalize(
          result.values().stream()
            .map(a -> a.get("dit"))
            .collect(Collectors.toList())
        )
      ),
      Map.entry(
        "nosi",
        normalize(
          result.values().stream()
            .map(a -> a.get("nosi"))
            .collect(Collectors.toList()))
      ),
      Map.entry(
        "rfc",
        normalize(
          result.values().stream()
            .map(a -> a.get("rfc"))
            .collect(Collectors.toList())
          )
      ),
      Map.entry(
        "wmc",
        normalize(
          result.values().stream()
            .map(a -> a.get("wmc"))
            .collect(Collectors.toList())
          )
      )
    );
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

    total = replaceFirstCommit(
      total, repository, branch, listOfCommits
    );

    total = fillNonModifiedFiles(
      total, 
      listOfCommits
    );

    return total;
  }

  private HashMap<String,MetricsData> fillNonModifiedFiles(
    HashMap<String,MetricsData> data,
    Commit[] commits) {
    
    for(var i = commits.length-1; i > 0; i--) {
      data.put(
        commits[i-1].getId().getName(),
        createData(
          commits[i], 
          commits[i-1],
          data
        )
      );
    }

    return data;
  }

  private MetricsData createData(
    Commit current, 
    Commit next, 
    HashMap<String,MetricsData> data) {
    
    var dataCurrent = data.get(
      current.getId().getName()
    );
    var dataNext = data.get(
      next.getId().getName()
    );

    var newData = new MetricsData();
    
    dataCurrent.getFiles().forEach((file,metrics) -> {

      if(!isDeletedFile(next.getDiffFiles(), file)) {
        newData.addFile(file, metrics);
      }
    });

    dataNext.getFiles().forEach((file,metrics) -> {
      newData.addFile(file, metrics);
    });

    return newData;
  }

  private Boolean isDeletedFile(List<DiffFile> listOfFiles, String file) {

    var result = false;
    if(listOfFiles == null) return result;

    for(var diff: listOfFiles) {
      if(diff.getOldPath().equals(file) &&
         diff.getType().equals("DELETE")) {

        result = true;
      }
    }
    return result;
  }

  private HashMap<String,MetricsData> replaceFirstCommit(
    HashMap<String,MetricsData> data,
    String repository,
    String branch,
    Commit[] listOfCommits) 
      throws GitRepositoryException, 
      GitAPIException {
    
    var firstCommit = listOfCommits[listOfCommits.length - 1]
      .getId().getName();

    data.put(
      firstCommit,
      player.metricsFromAllFilesInACommit(
        repository,branch,firstCommit
      )
    );

    return data;
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
      Map.entry("nosi", data[2]),//4
      Map.entry("rfc", data[3]),//5
      Map.entry("wmc", data[4])//6
    );
  }

  private Double[] normalize(List<Double> list) {

    var min = list.stream().min(Double::compare).get();
    var max = list.stream().max(Double::compare).get();

    return new Double[]{min,max};
  }

  

}
