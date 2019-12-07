package core.resource;

public class MetricsFilesResource extends Resource {

  MetricsFilesResource() {
    folder = "commits";
  }

  @Override
  public Resource setParams(String... params) {
    
    repository = params[0];
    file = params[1] + ".json";

    return this;
  }

  @Override
  protected String getBaseFolder() {
    
    return settings.folderOfMetrics().getAbsolutePath();
  }

}