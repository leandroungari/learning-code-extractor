package controller.mappings;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonObject;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.MappingsExtractor;
import ast.extractor.CommitMappings;
import core.controller.SettingsController;
import repository.Commit;
import repository.Repository;
import resource.JSON;
import resource.Resource;

public class MappingsController {

  private String repository;
  private String branch;

  public MappingsController(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
  }

  private List<Commit> getCommits() throws GitAPIException {
    var items = Repository.create(repository).branch(branch).commits();
    return items;
  }

  public Boolean extractMappingsToStore() throws GitAPIException {
    for (var item : getCommits()) {
      this.extractMappingsToStore(item.id().getName());
    }
    return true;
  }

  public Boolean extractMappingsToStore(String commit) {
    var extractor = new MappingsExtractor(repository, branch, commit);

    var result = extractor.createMappings();
    return this.store(result);
  }

  private Boolean store(CommitMappings item) {
    var resource = new Resource<JsonObject>(
      SettingsController.get().folderOfMetadata() + 
      "/" + repository + 
      "/mappings",
      item.getCommit() + ".json"
    );

    resource.setContent(item.toJson());

    try {
      resource.write(new JSON());
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
  
}