package core.features;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GetNumberOfFilesInCommitsByBranchTest {

  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void run() {

    var command = new GetNumberOfFilesInCommitsByBranch();
    command.setParams(REPOSITORY,BRANCH);

    var quantityOfFiles = command.action();

    assertEquals(65, quantityOfFiles.size());
  }
}