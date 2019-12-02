package core.features;

public class DownloadRepository implements RepositoryFeature<Void> {

  private String repository;
  private String url;

  @Override
  public Feature<Void> setParams(String... params) {

    repository = params[0];
    url = params[1];

    return this;
  }

  @Override
  public Void action() {

    createRepository(repository, url);
    return null;
  }

}