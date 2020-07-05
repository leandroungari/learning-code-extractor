package ast.cluster;

import java.io.IOException;

import persistence.Settings;
import persistence.resource.JSON;
import persistence.resource.Resource;

public class ClusterSetPersistor {

  private String base;
  private String repository;

  private ClusterSet cluster;

  private ClusterItemPersistor persistor;

  public ClusterSetPersistor(ClusterSet cluster) {

    this.repository = cluster.repository();
    this.cluster = cluster;

    this.base = Settings.get()
      .folderOfMetadata() + "/" + 
      this.repository + "/clusters";

    this.persistor = new ClusterItemPersistor(this.repository, cluster);
  }

  ClusterItemPersistor item() {
    return persistor;
  }

  public void write() {
    var resource = new Resource(this.base, "#" + repository + ".json");
    try {
      resource.write(new JSON<>(cluster), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ClusterSet read() {
    var resource = new Resource(this.base, "#" + repository + ".json");
    try {
      return resource.read(new JSON<>(cluster));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}