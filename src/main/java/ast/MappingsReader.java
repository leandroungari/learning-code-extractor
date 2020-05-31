package ast;

import java.io.IOException;

import com.google.gson.JsonObject;

import ast.extractor.CommitMappings;
import core.controller.SettingsController;
import resource.JSON;
import resource.Resource;

public class MappingsReader {

  private String repository;
  private String branch;

  public MappingsReader(String repositoryName, String branch) {
    this.repository = repositoryName;
    this.branch = branch;
  }

  public CommitMappings commit(String id) {
    System.out.println("-" + id);
    var result = new CommitMappings(repository, branch, id);
    var resource = new Resource<JsonObject>(
      SettingsController.get().folderOfMetadata() 
        + "/" + repository 
        + "/mappings/", 
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