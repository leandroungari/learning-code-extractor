package core.resource.resources;

import java.io.IOException;

import core.data.CommitInfo;
import core.resource.Resource;
import storage.Storage;

public class CommitsInfoResource extends Resource<CommitInfo[]> {

  private final String FILENAME = "info-commits.csv";

  private String repository;
  private String branch;

  public CommitsInfoResource() {

  }

  @Override
  public Resource<CommitInfo[]> setParams(String... params) {
    
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
  public CommitInfo[] read(Class<CommitInfo[]> type) {
    
    try {
      return storage
      .<CommitInfo>csv(FILENAME)
      .read(CommitInfo.class);
    }
    catch(IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void write(CommitInfo[] data) {
    
    try {
      storage
      .<CommitInfo>csv(FILENAME)
      .write(data);
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  

}