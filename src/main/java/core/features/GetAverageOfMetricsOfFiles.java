package core.features;

import java.util.Map;

import core.controller.DataController;

public class GetAverageOfMetricsOfFiles implements Feature<Map<String, Map<String, Double>>> {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;
  private Integer step;

  @Override
  public Feature<Map<String, Map<String, Double>>> setParams(String... params) {
    
    repository = params[0];
    branch = params[1];
    from = Integer.valueOf(params[2]);
    to = Integer.valueOf(params[3]);
    step = Integer.valueOf(params[4]);

    return this;
  }

  @Override
  public Map<String, Map<String, Double>> action() {
    

    return null;
  }

  private void getMetricsFromCommits() {
    player
    .<>
  }

}