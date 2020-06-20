package command;

import com.beust.jcommander.Parameter;

import org.eclipse.jgit.api.errors.GitAPIException;

import controller.mappings.MappingsController;

public class MappingsCommand implements Command {

  @Parameter(names = { "-repo" })
  private String repository;

  @Parameter(names = { "-branch" })
  private String branch;

  @Parameter(names = { "-from" })
  private Integer from;

  @Parameter(names = { "-to" })
  private Integer to;
  

  public void run() {
    MappingsController controller = null;
    if(from != null && to != null) {
      controller = new MappingsController(repository, branch, from, to);
    } else {
      controller = new MappingsController(repository, branch);
    }

    try {
      controller.extractMappingsToStore();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }
}