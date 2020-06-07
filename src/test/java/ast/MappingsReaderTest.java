package ast;

import org.junit.Test;

public class MappingsReaderTest {
  
  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void readD4BC() {
    var commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

    var reader = new MappingsReader(repository, branch);
    var result = reader.commit(commit);

    System.out.println(result);
  }

  @Test
  public void read63cbed() {
    var commit = "63cbed99a601e79c6a0ae389b2a57acdbd3e1b44";

    var reader = new MappingsReader(repository, branch);
    var result = reader.commit(commit);

    System.out.println(result);
  }

  
}