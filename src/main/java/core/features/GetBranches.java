package core.features;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.features.Feature;
import core.data.Branch;
import repository.exception.GitRepositoryException;

public class GetBranches extends Feature<Branch[]> {

  private String repository;

  public GetBranches() {
    super();
  }

  @Override
  public Feature<Branch[]> setParams(String... params) {
    
    repository = params[0];

    return this;
  }

  @Override
  public Branch[] action() {
    
    try {
    
		  return player
			  .repository(repository)
			  .branches();
    }
    catch(GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }

    return null;
  }
  
}