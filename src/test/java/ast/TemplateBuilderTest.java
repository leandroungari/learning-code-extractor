package ast;

import org.junit.BeforeClass;
import org.junit.Test;

import ast.extractor.CommitMappings;

public class TemplateBuilderTest {

  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private static final String commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  private static Extractor extractor;
  private static CommitMappings mappings;

  @BeforeClass
  public static void init() {

    extractor = new Extractor(repository, branch, commit);
    mappings = extractor.transform();
  }

  @Test
  public void createModelToFirstMapping() {
    var builder = new TemplateBuilder<>(mappings.get().get(0));
    var map = builder.model();
    System.out.println(map);
  }

  @Test
  public void createModelToSecondMapping() {
    var builder = new TemplateBuilder<>(mappings.get().get(1));
    var map = builder.model();
    System.out.println(map);
  }
}