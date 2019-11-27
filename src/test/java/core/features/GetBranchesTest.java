package core.features;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;

import core.Player;
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

    var repo = Repository.create(new File(
			player.settings().folderOfRepositories() + "/"
			+ REPOSITORY
		));
    
		var result = repo.branches();
      
    assertTrue(result != null);
  }
}