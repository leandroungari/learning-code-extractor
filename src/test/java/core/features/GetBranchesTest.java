package core.features;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class GetBranchesTest {
  
  private final String REPOSITORY = "refactoring-toy-example";

  @Test
  public void run() {

    var command = new GetBranches();
    command.setParams(REPOSITORY);

    var result = command.action();
    System.out.println(Arrays.toString(result));
    assertTrue(result.length == 3);
  }

  
}