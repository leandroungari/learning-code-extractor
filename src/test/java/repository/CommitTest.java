package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.Settings;
import repository.exception.GitRepositoryException;

public class CommitTest {

  private static final String REPOSITORY = "refactoring-toy-example";
  private static final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private static final String COMMIT = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private static final String PREVIOUS_COMMIT = "9a5c33b16d07d62651ea80552e8782974c96bb8a";
  private static final String FILENAME = ".project";

  private static Repository repo;
  private static Branch branch;
  private static Commit commit;

  @BeforeClass
  public static void start() 
    throws GitRepositoryException, 
           GitAPIException {

    repo = Repository.create(new File(
	    Settings.get().folderOfRepositories() + "/" +
	    REPOSITORY
    ));
    
    branch = repo.branch(BRANCH);
    commit = branch.commit(COMMIT);
  }

  @Test
  public void numberOfFilesByExtension() {
    
    var result = commit.allFilesByExtension(".project");
    assertEquals(1, result.size());
  }

  @Test
  public void diffInCommits() {

    var result = commit.diff();
    assertEquals(2, result.size());
  }

  @Test
  public void getEarlyCommit() {

    var before = commit.before();
    assertEquals(PREVIOUS_COMMIT, before.id().name());
  }

  @Test
  public void getFile() {

    var file = commit.getFile(FILENAME);
    assertTrue(file != null);
  }


}