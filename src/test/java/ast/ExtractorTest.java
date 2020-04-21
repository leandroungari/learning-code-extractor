package ast;

import org.junit.BeforeClass;
import org.junit.Test;

public class ExtractorTest {

  private static Extractor extractor;

  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private static final String commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @BeforeClass
  public static void start() {

    extractor = new Extractor(repository, branch, commit);
  }

  @Test
  public void transform() {

    var result = extractor.transformModifications();
    System.out.println(result);
  }
}