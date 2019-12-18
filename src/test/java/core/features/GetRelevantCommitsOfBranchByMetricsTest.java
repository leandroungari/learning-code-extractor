package core.features;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GetRelevantCommitsOfBranchByMetricsTest {

  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private final String TYPE = "sum";
  private final String METRIC = "wmc";

  @Test
  public void action() {
    
    var command = new GetRelevantCommitsOfBranchByMetrics();
    
    var result = command
      .setParams(REPOSITORY,BRANCH,METRIC,TYPE)
      .action();

    assertTrue(
      result.get("good").size() > 0 ||
      result.get("bad").size() > 0 
    );
  }
}