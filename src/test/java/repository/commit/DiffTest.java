package repository.commit;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;

import core.Settings;
import repository.Commit;
import repository.GitFactory;
import repository.Repository;
import repository.exception.GitRepositoryException;

public class DiffTest {

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
  public void gettingDiff() {

    var factory = new GitFactory(repo);
    var result = factory
      .contentOfDiffCommit(commit, commit.diff().get(0));

    System.out.println(result);
  }
}