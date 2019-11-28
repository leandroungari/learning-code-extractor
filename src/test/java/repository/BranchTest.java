package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;

import core.Player;
import repository.exception.GitRepositoryException;

public class BranchTest {

  private static final String REPOSITORY = "refactoring-toy-example";
  private static final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  private static Repository repo;
  private static Branch branch;

  private static Player player;

  @BeforeClass
  public static void start() 
    throws GitRepositoryException, 
           GitAPIException {
    
    player = new Player();

    repo = Repository.create(new File(
	    player.settings().folderOfRepositories() + "/" +
	    REPOSITORY
    ));
    
    branch = repo.branch(BRANCH);
  }

  @Test
  public void allCommits() {

    var result = branch
      .commits();

    assertTrue(result.size() == 65);
  }

  @Test
  public void lastCommit() {

    var lastCommit = branch
      .commits()
      .get(64);

    assertEquals(BRANCH, lastCommit.id().getName());
  }

  @Test
  public void rangeOfCommits() {

    var result = branch
      .commits(63, 64);

    assertTrue(result.size() == 2);
  }


}