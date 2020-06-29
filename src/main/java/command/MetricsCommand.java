package command;

import com.beust.jcommander.Parameter;

import controller.MetricsController;

public class MetricsCommand implements Command {

  @Parameter(names = { "-repo" })
  private String repository;

  @Parameter(names = { "-branch" })
  private String branch;

  @Parameter(names = { "-from" })
  private Integer from;

  @Parameter(names = { "-to" })
  private Integer to;

  public void run() {

    MetricsController controller = null;
    if(from != null && to != null) {
      controller = new MetricsController(repository, branch, from, to);
    } else {
      controller = new MetricsController(repository, branch);
    }
    
    controller.extract();
  }
}