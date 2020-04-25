package core.resource.resources;

import java.io.IOException;

import com.google.gson.JsonElement;

import core.resource.Resource;
import storage.Storage;

public class ModificationsResource 
  extends Resource<JsonElement> {

  private String repository;
  private String commit;

  @Override
  public Resource<JsonElement> setParams(String... params) {
    repository = params[0];
    commit = params[1];

    storage = new Storage(
      settings.folderOfMetadata().getAbsolutePath()
      + "/" + repository 
      + "/modifications"
    );

    storage.plainText(commit + ".json");

    return this;
  }

  @Override
  public JsonElement read(Class<JsonElement> type) {
    
    try {
      return storage
        .<JsonElement>json(commit + ".json")
        .read(JsonElement.class);
    }
    catch(IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void write(JsonElement data) {
    
    try {
      storage
      .json(commit + ".json")
      .write(data);
    }
    catch(IOException e) {
      e.printStackTrace();
    }

  }

    
}