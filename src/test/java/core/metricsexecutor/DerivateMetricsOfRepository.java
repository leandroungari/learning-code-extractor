package core.metricsexecutor;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import core.Player;
import core.SettingsExecutor;
import repository.exception.GitRepositoryException;

public class DerivateMetricsOfRepository {

  private String repository;

  @BeforeClass
  public static void create() {
    var settingExec = new SettingsExecutor();
  	settingExec.start(SettingsExecutor.USER_FOLDER);
  }

  @Before
  public void before() {
    repository = "refactoring-toy-example";
  }

  @Test
  public void start() throws GitAPIException, GitRepositoryException {
    
    var player = new Player();
    var stats = new Stats(player, repository);
    var result = stats.branch().derivateMetrics();

    System.out.println(result);
  }
}
