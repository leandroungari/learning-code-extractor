package core.features;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GetCommitsByBranchTest {
  
  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void run() {
    
    var command = new GetCommitsByBranch();
    command.setParams(REPOSITORY, BRANCH);
      
    var result = command.action();

    assertTrue(result.length == 65);
  }
}