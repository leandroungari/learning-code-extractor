package ast.cluster;

import java.io.IOException;

import persistence.Settings;
import persistence.resource.JSON;
import persistence.resource.Resource;

public class ItemPersistor {

  private String base;
  private String repository;

  public ItemPersistor(ClusterSet cluster) {

    this.repository = cluster.getRepository();

    this.base = Settings.get().folderOfMetadata() + 
      "/" + this.repository + "/clusters";
  }

  public void write(Item item) {
    var resource = new Resource(this.base, "#" + item.getCode() + ".json");
    try {
      resource.write(new JSON<>(item), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Item read(Item item) throws NullPointerException {
    var resource = new Resource(this.base, "#" + item.getCode() + ".json");
    try {
      return resource.read(new JSON<>(item));
    } catch (IOException e) {
      throw new NullPointerException();
    }
  }

  public void delete(Item item) {
    var resource = new Resource(this.base, "#" + item.getCode() + ".json");
    resource.delete();
  }
}