package core.resource;

import java.io.IOException;

import core.data.Branch;
import storage.Storage;

public class ListOfBranchesResource extends Resource<Branch[]> {

  private String repository;
  private final String FILENAME = "branch.csv";

  ListOfBranchesResource() {

  }

  @Override
  public Resource<Branch[]> setParams(String... params) {

    repository = params[0];

    storage = new Storage(
      settings.folderOfMetadata()
      .getAbsolutePath() + 
      "/" + repository + 
      "/"
    );

    return this;
  }

  @Override
  public Branch[] read(Class<Branch[]> type) {

    try {

      return storage
        .<Branch>csv(FILENAME)
        .read(Branch.class);

    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void write(Branch[] data) {
    
    try {
      
      storage
      .<Branch>csv(FILENAME)
      .addAll(data)
      .write();
    
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}