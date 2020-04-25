package ast.extractor;

import org.junit.Test;

public class ModificationsReaderTest {

  private static final String repository = "refactoring-toy-example";
  private static final String commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void readModifications() {

    var reader = new ModificationsReader(repository, commit);
    reader.read();
  }
}