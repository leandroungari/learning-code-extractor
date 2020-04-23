package core.resource.resources;

import java.io.IOException;

import ast.CommitModifications;
import core.resource.Resource;
import storage.Storage;

public class ModificationsResource 
  extends Resource<CommitModifications> {

  private String repository;
  private String commit;

  @Override
  public Resource<CommitModifications> setParams(String... params) {
    repository = params[0];
    commit = params[1];

    storage = new Storage(
      settings.folderOfMetadata().getAbsolutePath()
      + "/" + repository 
      + "/modifications/"
    );

    return this;
  }

  @Override
  public CommitModifications read(Class<CommitModifications> type) {
    
    try {
      var texto = storage
        .plainText(commit + ".json")
        .read();

      System.out.println("ModificationsResource.java");
      System.out.println(texto);
    }
    catch(IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void write(CommitModifications data) {
    
    try {
      storage
      .plainText(commit + ".json")
      .write(data.toString());
    }
    catch(IOException e) {
      e.printStackTrace();
    }

  }

    
}