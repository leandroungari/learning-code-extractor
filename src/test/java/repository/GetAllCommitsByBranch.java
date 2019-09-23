package repository;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;

import repository.exception.GitRepositoryException;


public class GetAllCommitsByBranch {

	private Repository repo;
	
	@Before
	public void before() 
		throws GitRepositoryException {
		
		repo = Repository
			.create(new File("/home/leandroungari/Documents/repositories/truth"));
	}
	
  @Test
  public void listAllBranches() 
  	throws GitAPIException {
    
  	repo
  	.branches()
  	.forEach(branch -> {
  		System.out.println(branch.name());
  	});
  }
  
  @Test
  public void allCommitsAtMaster() 
  	throws IOException {
  	
  	assertEquals(
  		1444, 
  		repo
    	.branch("refs/remotes/origin/master")
    	.commits()
    	.size()
    );
  	
  }
}
