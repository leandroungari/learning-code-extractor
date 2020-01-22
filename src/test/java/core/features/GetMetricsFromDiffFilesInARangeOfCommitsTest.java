package core.features;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;


public class GetMetricsFromDiffFilesInARangeOfCommitsTest {

  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private final String FROM = "63";
  private final String TO = "64";
  private final String STEP = "1";

  @Test
  public void run() {
    
    var command = new GetMetricsFromDiffFilesInARangeOfCommits();
    
    var result = command
      .setParams(REPOSITORY,BRANCH,FROM,TO,STEP)
      .action();

    var list = new ArrayList<>(result.values());

    assertTrue(list.get(0).size() == 2);
    assertTrue(list.get(1).size() == 2);
  }

  
}