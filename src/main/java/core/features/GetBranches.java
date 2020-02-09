package core.features;

import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import core.data.Branch;
import core.resource.Resource;
import core.resource.ResourceTrack;
import core.resource.ResourceType;
import repository.exception.GitRepositoryException;

public class GetBranches implements RepositoryFeature<Branch[]> {

  private String repository;

  private Resource resource;

  private ResourceTrack track = new ResourceTrack();

  @Override
  public Feature<Branch[]> setParams(String... params) {
    
    repository = params[0];

    return this;
  }

  @Override
  public Branch[] action() {
    
    try {
    
      resource = track
        .get(ResourceType.LIST_OF_BRANCHES)
        .setParams(repository);

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
    
    var repo = createRepository(repository);
    
	  var list = repo
	  	.branches()
	  	.stream()
	  	.map(branch -> new Branch(
	  		branch.name(), 
	  		branch.id().getName()
	  	))
	  	.collect(Collectors.toList());
    
	  resource.write(list); 
    
  }
  
}