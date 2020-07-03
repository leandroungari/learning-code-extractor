package controller.mappings;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.MappingsExtractor;
import ast.extractor.CommitMappings;
import ast.snapshot.Snapshot;
import persistence.Settings;
import repository.Commit;
import persistence.resource.JSON;
import persistence.resource.Resource;

public class MappingsController {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  private Snapshot snapshot;

  private String base;

  public MappingsController(String repository, String branch) throws GitAPIException {
    this.repository = repository;
    this.branch = branch;

    this.snapshot = Snapshot.create(repository, branch);
    this.base = Settings.get().folderOfMetadata().getAbsolutePath();
  }

  public MappingsController(String repository, String branch, Integer from, Integer to) 
    throws GitAPIException {
    
    this.repository = repository;
    this.branch = branch;
    this.from = from;
    this.to = to;

    this.snapshot = Snapshot.create(repository, branch);
    this.base = Settings.get().folderOfMetadata().getAbsolutePath();
  }

  private List<Commit> getCommits() throws GitAPIException {
    List<Commit> items = null;

    if(from != null && to != null) {
      items = snapshot.getBranch().commits(from, to);
    } else {
      items = snapshot.getBranch().commits();
      items.remove(0);
    }
    return items;
  }

  public Boolean extractMappingsToStore() throws GitAPIException {
    var counter = 0;
    for (var item : getCommits()) {
      System.err.println("> #" + counter + " - " + item.id().getName());
      this.extractMappingsToStore(item.id().getName());
      counter++;
    }
    return true;
  }

  public Boolean extractMappingsToStore(String commit) {

    var resource = new Resource(base + "/" + repository + "/mappings", commit + ".json");
    if(resource.exists()) return true;

    var extractor = new MappingsExtractor(repository, branch, commit);
    try {
      var result = extractor.createMappings();
      return this.store(result);
    } catch(Exception ex) {
      System.err.println("Erro na leitura do commit " + commit);
    }

    return false;
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