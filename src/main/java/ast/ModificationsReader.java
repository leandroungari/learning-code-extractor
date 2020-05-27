package ast;

import java.io.IOException;

import com.google.gson.JsonObject;

import ast.extractor.CommitModifications;
import core.controller.SettingsController;
import resource.JSON;
import resource.Resource;

public class ModificationsReader {

  private String repository;
  private String branch;

  public ModificationsReader(String repositoryName, String branch) {
    this.repository = repositoryName;
    this.branch = branch;
  }

  public CommitModifications commit(String id) {

    var result = new CommitModifications(repository, branch, id);
    var resource = new Resource<JsonObject>(
      SettingsController.get().folderOfMetadata() 
        + "/" + repository 
        + "/modifications", 
      id + ".json"
    );

    try {
      return result.fromJson(resource.read(new JSON()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}