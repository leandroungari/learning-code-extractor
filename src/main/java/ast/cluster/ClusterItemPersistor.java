package ast.cluster;

import java.io.IOException;

import ast.extractor.elements.ElementFactory;
import persistence.Settings;
import persistence.resource.JSON;
import persistence.resource.Resource;

public class ClusterItemPersistor {

  private String base;
  private String repository;

  private ClusterSet parent;

  public ClusterItemPersistor(String repository, ClusterSet parent) {

    this.repository = repository;
    this.parent = parent;

    this.base = Settings.get()
      .folderOfMetadata() + "/" + 
      this.repository + "/clusters";
  }

  void write(ClusterItem item) {
    var resource = new Resource(this.base, "#" + item.getCode() + ".json");
    System.out.println("> write cluster #" + item.getCode() + ".json");

    ElementFactory.disableLoadElement();
    try {
      resource.write(new JSON<ClusterItem>(item), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
    ElementFactory.enableLoadElement();
  }

  ClusterItem read(Integer code) {
    var resource = new Resource(this.base, "#" + code + ".json");
    System.out.println("> read cluster #" + code + ".json");

    ElementFactory.disableLoadElement();
    ClusterItem result = null;
    try {
      var item = new ClusterItem(code, parent);
      result = resource.read(new JSON<ClusterItem>(item));
    } catch (IOException e) {
      e.printStackTrace();
    }
    ElementFactory.enableLoadElement();
    return result;
  }

}