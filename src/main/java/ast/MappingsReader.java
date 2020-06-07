package ast;

import java.io.IOException;

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
    var result = new CommitMappings(repository, branch, id);
    var resource = new Resource(
      SettingsController.get().folderOfMetadata() 
        + "/" + repository 
        + "/mappings/", 
      id + ".json"
    );

    try {
      return resource.<CommitMappings>read(new JSON<>(result));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}