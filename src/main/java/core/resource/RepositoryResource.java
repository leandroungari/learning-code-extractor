package core.resource;

public class RepositoryResource extends Resource {

  private String repository;
  private String folder;
  private String file;

  RepositoryResource(String repository, String folder, String file) {
    super();
    this.repository = repository;
    this.folder = folder;
    this.file = file;
  }

  @Override
  public String getFolder() {
    
    return settings.folderOfRepositories() + "/" + repository + "/" + folder;
  }

  @Override
  public String getFile() {

    return file;
  }
}