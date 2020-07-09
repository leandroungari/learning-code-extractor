package command;

import com.beust.jcommander.Parameter;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.ClusterExtractor;
import controller.ClusterExtractorController;

public class ClusterExtractorCommand implements Command {

  @Parameter(names = { "-repo" })
  private String repository;

  @Parameter(names = { "-branch" })
  private String branch;

  public void run() {

    var controller = new ClusterExtractorController(repository, branch);
    controller.execute();

  }
}