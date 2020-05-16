package ast;

import org.junit.Test;

public class ExtractorTest {

  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void transform() {
    var commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

    var extractor = new Extractor(repository, branch, commit);
    var result = extractor.transformModifications();
    System.out.println(result);
  }
}