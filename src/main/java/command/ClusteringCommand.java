package command;

import com.beust.jcommander.Parameter;

import controller.mappings.ClusteringController;

public class ClusteringCommand implements Command {
  
  @Parameter(names={"-repo"})
  private String repository;

  @Parameter(names={"-branch"})
  private String branch;

  @Parameter(names={"-from"})
  private Integer from;

  @Parameter(names={"-to"})
  private Integer to;

  public void run() {
    if(from != null && to != null) {
      var controller = new ClusteringController(repository, branch, from, to);
      controller.clusterToStore();
    } else {
      var controller = new ClusteringController(repository, branch);
      controller.clusterToStore();
    }
  }
}