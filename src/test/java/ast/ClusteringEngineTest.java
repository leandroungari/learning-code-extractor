package ast;

import org.junit.Test;

public class ClusteringEngineTest {
  
  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void generate() {

    var engine = new ClusteringEngine(repository, branch);
    engine.generate();
  }
}