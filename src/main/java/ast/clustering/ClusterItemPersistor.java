package ast.clustering;

import java.io.IOException;

import com.google.gson.JsonObject;

import core.controller.SettingsController;
import resource.JSON;
import resource.Resource;

public class ClusterItemPersistor {

  private String base;
  private String repository;

  private Cluster parent;

  public ClusterItemPersistor(String repository, Cluster parent) {

    this.repository = repository;
    this.parent = parent;

    this.base = SettingsController.get()
      .folderOfMetadata() + "/" + 
      this.repository + "/clusters";
  }

  void write(ClusterItem item) {
    var resource = new Resource<JsonObject>(this.base, "#" + item.getCode() + ".json");
    System.out.println(this.base + "/#" + item.getCode() + ".json");
    resource.setContent(item.toJson());
    try {
      resource.write(new JSON(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  ClusterItem read(Integer code) {
    var resource = new Resource<JsonObject>(this.base, "#" + code + ".json");
    try {
      var object = resource.read(new JSON());
      return new ClusterItem(code, parent).fromJson(object);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}