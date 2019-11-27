package core.features;

import static org.junit.Assert.assertTrue;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;

import core.Player;
import repository.exception.GitRepositoryException;

public class GetCommitsByBranchTest {
  
  private Player player;
  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Before
  public void before() {

    player = new Player();
  }

  @Test
  public void run() 
    throws GitAPIException, GitRepositoryException {
    
    var result = player
      .repository(REPOSITORY)
      .commits(BRANCH);
      
    assertTrue(result != null);
  }
}