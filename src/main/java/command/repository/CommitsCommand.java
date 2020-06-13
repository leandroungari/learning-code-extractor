package command.repository;

import com.beust.jcommander.Parameter;

import command.Command;
import controller.RepositoryController;

public class CommitsCommand implements Command {
  
  @Parameter(names={"-repo"})
  private String repository;

  @Parameter(names={"-branch"})
  private String branch;

  public void run() {
    var controller = new RepositoryController(repository);
    controller.commits(branch);
  }
}