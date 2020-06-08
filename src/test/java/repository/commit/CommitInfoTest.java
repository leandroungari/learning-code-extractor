package repository.commit;

import static org.junit.Assert.assertTrue;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.Settings;
import repository.Commit;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class CommitInfoTest {

  private static Repository repo;
  private static Commit commit;

  private static final String REPOSITORY = "refactoring-toy-example";
  private static final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private static final String COMMIT = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @BeforeClass
  public static void start() 
    throws GitRepositoryException, GitAPIException {

    repo = Repository.create(new java.io.File(
	    Settings.get().folderOfRepositories() + "/" +
	    REPOSITORY
    ));
    
    commit = repo.branch(BRANCH).commit(COMMIT);
  }

  @Test
  public void getListOfFiles() {

    var list = commit.allFiles();
    assertTrue(list.size() > 0);
  }

  @Test
  public void getListOfFilesByExtension() {

    var list = commit.allFilesByExtension(".java");
    assertTrue(list.size() > 0);
  }

  @Test
  public void getDiffs() {

    var list = commit.diff();
    assertTrue(list.size() > 0);
  }

  @Test
  public void getFileInCommit() {
    var file = commit.getFile(".project");
    assertTrue(file != null);
  }

  @Test
  public void getCommitInfo() {     
    var info = commit.info();

    assertTrue(info.getAuthorName().equals("Danilo F. Silva"));
  }


}