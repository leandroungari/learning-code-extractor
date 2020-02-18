package core.features;

import org.junit.Test;

public class GetFileContentTest {

  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private final String COMMIT = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private final String FILE = "src/org/reptile/TurtleMarinha.java";

  @Test
  public void test() {

    var command = new GetFileContent();
    command.setParams(REPOSITORY, BRANCH, COMMIT, FILE);

    var result = command.action();
    System.out.println(result);
  }
}