package repository;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;

import core.controller.SettingsController;
import repository.exception.GitRepositoryException;

public class RepositoryTest {

  private static SettingsController settings;

  private static Repository repo;
  private static final String REPOSITORY = "refactoring-toy-example";

  private static final String MASTER_ID = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @BeforeClass
  public static void start() throws GitRepositoryException {

    settings = SettingsController.get();

    repo = Repository.create(new File(
      settings.folderOfRepositories() + 
      "/" + REPOSITORY
    ));
  }

  @Test
  public void getBranch() throws GitAPIException {
    
    var branch = repo.branch(MASTER_ID);
    assertTrue(branch != null);
  }

  @Test
  public void getBranches() throws GitAPIException {

    var quantity = repo
      .branches()
      .size();

    assertTrue(quantity > 0);
  }

  /**
   * Create test to clone and create
   * methods
   */
}