package metrics;

import java.io.IOException;

import persistence.Settings;
import resource.CSV;
import resource.Resource;

public class QualityMetricsPersistor {

  private String repository;
  private String branch;

  private String base;

  public QualityMetricsPersistor(String repository, String branch) {

    this.repository = repository;
    this.branch = branch;

    this.base = Settings.get()
      .folderOfMetadata() + "/" + 
      this.repository + "/metrics";
  }

  void write(CommitMetrics item, String commit) {
    var resource = new Resource(this.base + "/" + branch, commit + ".csv");
    try {
      resource.write(new CSV<>(item));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  CommitMetrics read(String commit) {
    var resource = new Resource(this.base + "/" + branch, commit + ".csv");
    var item = new CommitMetrics();
    try {
      return resource.read(new CSV<>(item));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
