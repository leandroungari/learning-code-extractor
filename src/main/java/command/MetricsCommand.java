package command;

import com.beust.jcommander.Parameter;

import controller.MetricsController;

public class MetricsCommand implements Command {

  @Parameter(names = { "-repo" })
  private String repository;

  @Parameter(names = { "-branch" })
  private String branch;

  public void run() {
    var controller = new MetricsController(repository, branch);
    controller.extract();
  }
}