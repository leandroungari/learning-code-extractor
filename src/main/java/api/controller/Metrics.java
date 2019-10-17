package api.controller;

import java.io.File;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import repository.metrics.MetricsAnalyzer;
import repository.metrics.MetricsData;
import util.FileUtil;

@RestController()
public class Metrics {

  @GetMapping("/metrics")
  @ResponseBody
  public String index() {
    
    return "This controller returns results related to code quality metrics."; 
  }
  
  @GetMapping("/metrics/{repositoryName}/{branch}/{commit}")
  @ResponseBody
  public MetricsData data(
    @PathVariable String repositoryName,
    @PathVariable String branch,
    @PathVariable String commit) {
    
    var analyzer = new MetricsAnalyzer();
    
    analyzer
    .repositoryName(repositoryName)
    .enableLogs(false)
    .localFolder(new File(
      "/home/leandroungari/Documents/data/metrics"
    ));
    
    return analyzer.get( 
      branch, 
      commit
    );
  }
  
  @GetMapping("/metrics/listRepositories")
  @ResponseBody
  public ArrayList<String> list() {
    
    return FileUtil.listOfFolders(new File(
      "/home/leandroungari/Documents/data/metrics"
    ));
  }
  
}
