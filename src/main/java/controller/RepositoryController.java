package controller;

import java.io.File;

import persistence.Settings;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class RepositoryController {

  private String name;

  public RepositoryController(String repo) {
    this.name = repo;
  }

  public Boolean download(String url) {
    var folder = new File(Settings.get().folderOfRepositories() + "/" + name);
    try {
      Repository.create(url, folder);
    } catch (GitRepositoryException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}