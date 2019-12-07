package core.resource;

public class ListOfBranchesResource extends Resource {

  ListOfBranchesResource() {
    file = "branches.json";
  }

  @Override
  protected String getBaseFolder() {
    
    return settings.folderOfMetadata().getAbsolutePath();
  }

  @Override
  public Resource setParams(String... params) {
    
    repository = params[0];

    return this;
  }

}