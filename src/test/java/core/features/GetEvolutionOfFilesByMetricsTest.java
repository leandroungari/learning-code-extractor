package core.features;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GetEvolutionOfFilesByMetricsTest {

  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private final String FROM = "0";
  private final String TO = "64";
  private final String STEP = "4";
  private final String METRIC = "wmc";

  @Test
  public void run() {

    var command = new GetEvolutionOfFilesByMetrics();
    
    var result = command
      .setParams(REPOSITORY,BRANCH,FROM,TO,STEP,METRIC)
      .action();

    assertTrue(result != null);
  }
}