package command;

import com.beust.jcommander.Parameter;

import org.eclipse.jgit.api.errors.GitAPIException;

import controller.ObtainCommitInfoController;

public class ObtainCommitInfoCommand implements Command {

  @Parameter(names = { "-repo" })
  private String repository;

  @Parameter(names = { "-branch" })
  private String branch;

  @Override
  public void run() {

    var controller = new ObtainCommitInfoController(repository, branch);
    try {
      controller.obterInfo();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }
}