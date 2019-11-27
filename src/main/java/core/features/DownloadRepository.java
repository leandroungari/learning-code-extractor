package core.features;

import core.Feature;
import repository.exception.GitRepositoryException;

public class DownloadRepository extends Feature<Void> {

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

    try {
    
      player
      .repository(repository)
      .download(url);
    
    } catch (GitRepositoryException e) {
      e.printStackTrace();
    }
    
    return null;
  }

}