package ast.multipattern;

import java.io.IOException;

import persistence.Settings;
import persistence.resource.JSON;
import persistence.resource.Resource;

public class MultiPatternPersistor {
  
  private String repository;

  private String base;

  public MultiPatternPersistor(String repository) {
    this.repository = repository;

    this.base = Settings.get().folderOfMetadata() + 
      "/" + this.repository + "/" + "multipattern";
  }

  public void write(MultiPattern item, Integer pos, String currentCommit) {
    var resource = new Resource(this.base + "/" + pos + " - " + currentCommit,  "#" + item.getCode() + ".json");
    try {
      resource.write(new JSON<>(item));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public MultiPattern read(Integer code) {
    var resource = new Resource(this.base, "#" + code + ".json");
    try {
      return resource.read(new JSON<>(new MultiPattern()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}