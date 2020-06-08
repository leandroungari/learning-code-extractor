package command.repository;

import com.beust.jcommander.Parameter;

import controller.mappings.RepositoryController;

public class DownloadCommand {
  
  @Parameter(names={"-repo"})
  private String repository;

  @Parameter(names={"-url"})
  private String url;

  public void run() {
    var controller = new RepositoryController(repository);
    if(controller.download(url)) {
      System.out.println("Repositório clonado com sucesso!");
    } else {
      System.out.println("Aconteceu algum problema!");
    }
  }
}