package command;

import com.beust.jcommander.Parameter;

import org.eclipse.jgit.api.errors.GitAPIException;

import controller.mappings.MappingsController;

public class MappingsCommand {

  @Parameter(names = { "-repo" })
  private String repository;

  @Parameter(names = { "-branch" })
  private String branch;

  public void run() {
    var controller = new MappingsController(repository, branch);
    try {
      controller.extractMappingsToStore();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }
}