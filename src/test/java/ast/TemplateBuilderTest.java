package ast;

import org.junit.BeforeClass;
import org.junit.Test;

import ast.extractor.CommitModifications;
import ast.extractor.mapping.Mapping;

public class TemplateBuilderTest {

  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private static final String commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  private static Extractor extractor;
  private static CommitModifications modifications;

  private static Mapping firstMapping;

  @BeforeClass
  public static void init() {

    extractor = new Extractor(repository, branch, commit);
    modifications = extractor.transformModifications();

    var list = modifications.getModifications();

    firstMapping = list.get(0).getMappings().get(0);
  }

  @Test
  public void createModel() {
    var builder = new TemplateBuilder<>(firstMapping);
    var map = builder.model();
    System.out.println(map);
  }
}