package core.features;

import java.io.File;

import core.features.Feature;
import repository.Repository;
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
    
      Repository.create(
		  	url, 
		  	new File(
		  		player.settings().folderOfRepositories() + "/" +
		  		repository
		  	)
		  );
    
    } catch (GitRepositoryException e) {
      e.printStackTrace();
    }
    
    return null;
  }

}