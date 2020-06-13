package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import org.eclipse.jgit.api.errors.GitAPIException;

import persistence.Settings;
import persistence.resource.PlainText;
import persistence.resource.Resource;
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

  public void commits(String branch) {
    try {
      var result = Repository
        .create(name)
        .branch(branch)
        .commits()
        .stream()
        .map(item -> item.id().getName())
        .collect(Collectors.toList());

      this.toStore(branch, result);
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }

  private void toStore(String branch, List<String> result) {
    var base = Settings.get().folderOfMetadata();
    var resource = new Resource(base.getAbsolutePath(), branch + ".commits.json");
    var gson = new Gson();
    try {
      resource.write(new PlainText(gson.toJson(result)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}