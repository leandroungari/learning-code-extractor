package core;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import core.Player;
import core.SettingsExecutor;
import repository.exception.GitRepositoryException;

public class DerivateMetricsOfRepository {

  private String repository;
  private Player player;

  @BeforeClass
  public static void create() {
    var settingExec = new SettingsExecutor();
    settingExec.start(SettingsExecutor.USER_FOLDER);
    
  }

  @Before
  public void before() {
    repository = "refactoring-toy-example";
    player = new Player();
  }

  @Test
  public void branch() throws GitAPIException, GitRepositoryException {
    
    
    var stats = new Stats(player, repository);
    var result = stats.branch().derivate();

    System.out.println(result);
  }

  @Test
  public void commits() {
    var stats = new Stats(player, repository);
    var result = stats
      .commits("f3b628f8f9c71a2cdfa052025c4a1ed78ee4c45d")
      .derivate();

    System.out.println(result);
  }
}
