package metrics;

import java.io.IOException;
import java.util.List;

import ast.clustering.ClusterItem;
import core.controller.SettingsController;
import resource.JSON;
import resource.Resource;

public class QualityMetricsPersistor {

  private String repository;
  private String branch;

  private String base;

  public QualityMetricsPersistor(String repository, String branch) {

    this.repository = repository;
    this.branch = branch;

    this.base = SettingsController.get()
      .folderOfMetadata() + "/" + 
      this.repository + "/metrics";
  }

  void write(CommitMetrics item, String commit) {
    var resource = new Resource(this.base + "/" + branch, "#" + commit + ".csv");
    //fazer escrita
    resource.write(new );
  }

  CommitMetrics read(String commit) {
    var resource = new Resource(this.base, "#" + commit + ".json");
    var item = new ClusterItem(code, parent);
    //fazer read
    return null;
  }
}
