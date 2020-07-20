package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import org.eclipse.jgit.api.errors.GitAPIException;

import persistence.Settings;
import persistence.resource.PlainText;
import persistence.resource.Resource;
import repository.Repository;

public class ObtainCommitInfoController {

  private String repository;
  private String branch;

  public ObtainCommitInfoController(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
  }

  public void obterInfo() throws GitAPIException {

    var result = Repository
      .create(repository)
      .branch(branch)
      .commits()
      .stream()
      .map(item -> {
        var info = item.info();
        var map = Map.of(
          "email", info.getEmail(),
          "data", info.getDate()
        );
        return map;
      })
      .collect(Collectors.toList());

      this.toStore(result);
  }

  private void toStore(List<Map<String,String>> result) {
    var base = Settings.get().folderOfMetadata();
    var resource = new Resource(base.getAbsolutePath() + "/" + repository, "commits-info.json");
    var gson = new Gson();
    try {
      resource.write(new PlainText(gson.toJson(result)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}