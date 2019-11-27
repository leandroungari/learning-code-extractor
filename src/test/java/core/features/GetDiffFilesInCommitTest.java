package core.features;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;

import core.Player;
import repository.exception.GitRepositoryException;

public class GetDiffFilesInCommitTest {

  private Player player;
  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private final String COMMIT = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Before
  public void before() {
    player = new Player();
  }

  public void run() {

    try {

      player
      .repository(REPOSITORY)
      .diffFilesFromCommit(BRANCH, COMMIT);

    } catch (GitRepositoryException | GitAPIException e) {
      e.printStackTrace();
    }
  }
}