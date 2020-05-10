package ast.snapshot;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

public class SnapshotTest {

  private final String repository = "refactoring-toy-example";
  private final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private final String commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void createSnapshot() throws GitAPIException {
    var snapshot = Snapshot
      .create(repository, branch, commit);

  }

}