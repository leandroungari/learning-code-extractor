package core;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;

import repository.exception.GitRepositoryException;

public class DerivateMetricsOfRepository {

  private String repository;
  private Player player;

  @Before
  public void before() {
    player = new Player();
    repository = "refactoring-toy-example";
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
