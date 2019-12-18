package core.features;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GetDiffFilesInCommitTest {

  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private final String COMMIT = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void run() {

    var command = new GetDiffFilesInCommit();
    command.setParams(REPOSITORY, BRANCH, COMMIT);

    var result = command.action();

    assertTrue(result != null);
  }
}