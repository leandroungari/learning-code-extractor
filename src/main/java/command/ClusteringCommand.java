package command;

import com.beust.jcommander.Parameter;

import controller.mappings.ClusteringController;

public class ClusteringCommand implements Command {
  
  @Parameter(names={"-repo"})
  private String repository;

  @Parameter(names={"-branch"})
  private String branch;

  public void run() {
    var controller = new ClusteringController(repository, branch);
    controller.clusterToStore();
  }
}