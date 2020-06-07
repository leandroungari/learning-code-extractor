package ast.clustering;

import java.io.IOException;

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
    var resource = new Resource(this.base, "#" + repository + ".json");
    try {
      resource.write(new JSON<>(cluster), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}