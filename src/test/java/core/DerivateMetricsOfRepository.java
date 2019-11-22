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
  public void repository() throws GitAPIException, GitRepositoryException {
    
    var stats = new Stats(player);
    var result = stats.repository(repository).derivate("sum");

    System.out.println(result);
  }

  @Test
  public void branch() {
    var stats = new Stats(player);
    var result = stats
      .repository(repository)
      .branch("f3b628f8f9c71a2cdfa052025c4a1ed78ee4c45d")
      .derivate("sum");

    System.out.println(result);
  }
}
