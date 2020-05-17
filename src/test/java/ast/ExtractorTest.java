package ast;

import org.junit.Test;

public class ExtractorTest {

  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void commitD4BC() {
    var commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    var extractor = new Extractor(repository, branch, commit);
    var result = extractor.extract();
    System.out.println(result);
  }

  @Test
  public void commit0a46() {
    var commit = "0a46ed5c56c8b1576dfc92f3ec5bc2f0ea68aafe";

    var extractor = new Extractor(repository, branch, commit);
    var result = extractor.extract();
    System.out.println(result);
  }
}