package command;

import com.beust.jcommander.Parameter;

import org.eclipse.jgit.api.errors.GitAPIException;

import controller.MultiPatternController;

public class MultiPatternCommand implements Command {

  @Parameter(names = { "-repo" })
  private String repository;

  @Parameter(names = { "-branch" })
  private String branch;

  @Parameter(names = { "-from" })
  private Integer from;

  @Parameter(names = { "-to" })
  private Integer to;

  public void run() {

    var controller = new MultiPatternController(repository, branch, from, to);
    try {
      controller.execute();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    
  }
}