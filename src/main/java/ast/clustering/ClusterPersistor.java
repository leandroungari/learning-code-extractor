package ast.clustering;

import java.io.IOException;

import com.google.gson.JsonObject;

import core.controller.SettingsController;
import resource.JSON;
import resource.Resource;

public class ClusterPersistor {

  private String base;
  private String repository;

  private Cluster cluster;

  private ClusterItemPersistor persistor;

  public ClusterPersistor(Cluster cluster) {

    this.repository = cluster.repository();
    this.cluster = cluster;

    this.base = SettingsController.get()
      .folderOfMetadata() + "/" + 
      this.repository + "/clusters";

    this.persistor = new ClusterItemPersistor(this.repository, cluster);
  }

  ClusterItemPersistor item() {
    return persistor;
  }

  void write() {
    var resource = new Resource<JsonObject>(this.base, "#" + repository + ".json");
    resource.setContent(cluster.toJson());
    try {
      resource.write(new JSON(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}