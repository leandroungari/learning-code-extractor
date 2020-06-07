package ast.clustering;

import java.io.IOException;

import core.Settings;
import resource.JSON;
import resource.Resource;

public class ClusterItemPersistor {

  private String base;
  private String repository;

  private Cluster parent;

  public ClusterItemPersistor(String repository, Cluster parent) {

    this.repository = repository;
    this.parent = parent;

    this.base = Settings.get()
      .folderOfMetadata() + "/" + 
      this.repository + "/clusters";
  }

  void write(ClusterItem item) {
    var resource = new Resource(this.base, "#" + item.getCode() + ".json");
    System.out.println(this.base + "/#" + item.getCode() + ".json");
    try {
      resource.write(new JSON<ClusterItem>(item), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  ClusterItem read(Integer code) {
    var resource = new Resource(this.base, "#" + code + ".json");
    try {
      var item = new ClusterItem(code, parent);
      return resource.read(new JSON<ClusterItem>(item));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}