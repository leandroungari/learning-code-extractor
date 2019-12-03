package repository.commit;

import static org.junit.Assert.assertTrue;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;

import core.controller.SettingsController;
import repository.Branch;
import repository.Commit;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class FileTest {

  private static SettingsController settings;

  private static Repository repo;
  private static Branch branch;
  private static Commit commit;
  private static File file;

  private static final String REPOSITORY = "refactoring-toy-example";
  private static final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private static final String COMMIT = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private static final String FILENAME = "src/org/DogManager.java";

  @BeforeClass
  public static void start() 
    throws GitRepositoryException, GitAPIException {
    
    settings = new SettingsController();

    repo = Repository.create(new java.io.File(
	    settings.folderOfRepositories() + "/" +
	    REPOSITORY
    ));
    
    branch = repo.branch(BRANCH);
    commit = branch.commit(COMMIT);
    file = commit.getFile(FILENAME);
  }

  @Test
  public void getContentOfFile() {

    var content = file.get();
    assertTrue(content != null);
  }
}