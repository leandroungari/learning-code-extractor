package core.resource.resources;

import java.io.IOException;

import core.data.MetricsData;
import core.resource.Resource;
import storage.Storage;

public class MetricsFilesResource extends Resource<MetricsData[]> {

  private String repository;
  private String commit;

  public MetricsFilesResource() {

  }

  @Override
  public Resource<MetricsData[]> setParams(String... params) {

    repository = params[0];
    commit = params[1];

    storage = new Storage(
      settings.folderOfMetrics() + 
      "/" + repository +
      "/"
    );

    return this;
  }

  @Override
  public MetricsData[] read(Class<MetricsData[]> type) {
    
    try {
      
      return storage
        .<MetricsData>csv(commit + ".csv")
        .read(MetricsData.class);

    } catch(IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void write(MetricsData[] data) {
  
    try {
      
      storage
      .<MetricsData>csv(commit + ".csv")
      .write(data);
    
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}