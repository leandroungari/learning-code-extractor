package command;

import com.beust.jcommander.Parameter;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.ClusterExtractor;

public class ClusterExtractorCommand implements Command {

  @Parameter(names = { "-repo" })
  private String repository;

  @Parameter(names = { "-branch" })
  private String branch;

  public void run() {
    try {

      var controller = new ClusterExtractor(repository, branch);
      controller.execute();

    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }
}