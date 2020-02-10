package core.resource;

import java.io.IOException;

import core.data.Commit;
import storage.Storage;

public class ListOfCommitsResource extends Resource<Commit[]> {

  private final String FILENAME = "commits.json";

  private String repository;
  private String branch;

  ListOfCommitsResource() {
    
  }

  @Override
  public Resource<Commit[]> setParams(String... params) {
  
    repository = params[0];
    branch = params[1];

    storage = new Storage(
      settings.folderOfMetadata()
      .getAbsolutePath() +
      "/" + repository + 
      "/" + branch + 
      "/"
    );

    return this;
  }

  @Override
  public Commit[] read(Class<Commit[]> type) {
    
    try {
      return storage
      .<Commit[]>json(FILENAME)
      .read(Commit[].class);
    }
    catch(IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void write(Commit[] data) {
    
    try {
      storage
      .<Commit[]>json(FILENAME)
      .write(data);
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

}