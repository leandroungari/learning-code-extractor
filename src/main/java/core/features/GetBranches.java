package core.features;

import java.io.File;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.features.Feature;
import core.resource.Resource;
import core.resource.ResourceType;
import core.data.Branch;
import repository.Repository;
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
    
		  var resource = Resource.create(
        repository, 
        ResourceType.LIST_OF_BRANCHES
      );

      if (!resource.exists()) {
        createResource();
      }

      return resource.read(Branch[].class);
    }
    catch(GitAPIException | GitRepositoryException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void createResource() 
    throws GitRepositoryException, 
           GitAPIException {
    
    var repo = Repository.create(new File(
	    player.settings().folderOfRepositories() + "/" +
	    repository
	  ));
    
	  var list = repo
	  	.branches()
	  	.stream()
	  	.map(branch -> new Branch(
	  		branch.name(), 
	  		branch.id()
	  	))
	  	.collect(Collectors.toList());
    
	  Resource
	  .create(
	    repository, 
	    ResourceType.LIST_OF_BRANCHES
	  )
	  .write(list); 
    
  }
  
}