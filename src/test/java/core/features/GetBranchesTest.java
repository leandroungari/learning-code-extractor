package core.features;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;

import core.Player;
import core.data.Branch;
import core.resource.Resource;
import core.resource.ResourceType;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class GetBranchesTest {
  
  private Player player;
  private final String REPOSITORY = "refactoring-toy-example";

  @Before
  public void before() {

    player = new Player();
  }

  @Test
  public void run() 
    throws GitAPIException, GitRepositoryException {

    var resource = Resource.create(
      REPOSITORY, 
      ResourceType.LIST_OF_BRANCHES
    );

    if (!resource.exists()) {
      createResource();
    }

    var result = resource
      .read(Branch[].class);
      
    assertTrue(result != null);
  }

  private void createResource() 
    throws GitRepositoryException, 
           GitAPIException {
    
    var repo = Repository.create(new File(
	    player.settings().folderOfRepositories() + "/" +
	    REPOSITORY
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
	    REPOSITORY, 
	    ResourceType.LIST_OF_BRANCHES
	  )
	  .write(list); 
    
  }
}