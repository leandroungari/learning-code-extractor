package ast;

import org.junit.Test;

public class ModificationsBuilderTest {
  
  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private static final String commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void entireCommit() {
    //extrair as modificações de um commit inteiro
    //depois verificar como melhorar a análise
    var builder = new ModificationsBuilder(repository, branch, commit);
    var result = builder.createModifications();
    System.out.println(result);
  }
}