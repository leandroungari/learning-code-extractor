package core.resource;

public class ListOfCommitsResource extends Resource {

  ListOfCommitsResource() {
    file = "commits.json";
  }

  @Override
  public Resource setParams(String... params) {
  
    repository = params[0];
    folder = params[1];

    return this;
  }

  @Override
  protected String getBaseFolder() {
    
    return settings.folderOfMetadata().getAbsolutePath();
  }

}