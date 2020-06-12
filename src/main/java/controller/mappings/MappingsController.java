package controller.mappings;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.MappingsExtractor;
import ast.extractor.CommitMappings;
import persistence.Settings;
import repository.Commit;
import repository.Repository;
import persistence.resource.JSON;
import persistence.resource.Resource;

public class MappingsController {

  private String repository;
  private String branch;

  public MappingsController(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
  }

  private List<Commit> getCommits() throws GitAPIException {
    var items = Repository
      .create(repository)
      .branch(branch)
      .commits();

    items.remove(0);
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
    var resource = new Resource(
      Settings.get().folderOfMetadata() + 
      "/" + repository + 
      "/mappings",
      item.getCommit() + ".json"
    );

    try {
      resource.write(new JSON<CommitMappings>(item));
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
  
}