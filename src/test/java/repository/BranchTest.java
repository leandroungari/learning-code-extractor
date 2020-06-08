package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.Settings;
import repository.exception.GitRepositoryException;

public class BranchTest {

  private static final String REPOSITORY = "refactoring-toy-example";
  private static final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  private static Repository repo;
  private static Branch branch;

  @BeforeClass
  public static void start() 
    throws GitRepositoryException, 
           GitAPIException {

    repo = Repository.create(new File(
	    Settings.get().folderOfRepositories() + "/" +
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