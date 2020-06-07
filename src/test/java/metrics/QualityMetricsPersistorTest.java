package metrics;

import org.junit.Test;

public class QualityMetricsPersistorTest {
  
  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void readD4BC() {
    var commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    var persistor = new QualityMetricsPersistor(repository, branch);
    var item = persistor.read(commit);
    System.out.println(item);
  }
}