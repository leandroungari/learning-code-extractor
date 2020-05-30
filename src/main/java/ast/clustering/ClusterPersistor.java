package ast.clustering;

import java.io.IOException;

import com.google.gson.JsonObject;

import core.controller.SettingsController;
import resource.JSON;
import resource.Resource;

public class ClusterPersistor {

  private String base;
  private String repository;

  private ClusterItemPersistor persistor;

  public ClusterPersistor(String repository) {

    this.repository = repository;

    this.base = SettingsController.get()
      .folderOfMetadata() + "/" + 
      this.repository + "/clusters";

    this.persistor = new ClusterItemPersistor(repository);
  }

  ClusterItemPersistor item() {
    return persistor;
  }

  void write(Cluster cluster) {
    var resource = new Resource<JsonObject>(this.base, "#" + repository + ".json");
    System.out.println(this.base + "/#" + repository + ".json");
    resource.setContent(cluster.toJson());
    try {
      resource.write(new JSON(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}