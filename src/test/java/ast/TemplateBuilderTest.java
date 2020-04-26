package ast;

import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.*;
import org.junit.BeforeClass;
import org.junit.Test;

import ast.extractor.Mapping;

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
  public void getVariablesBeforeModification() {

    var builder = new TemplateBuilder<>(firstMapping);
    var variables = builder.before().variables();
    assertThat(variables.size(), equalTo(0));
  }

  @Test
  public void getVariablesAfterModification() {

    var builder = new TemplateBuilder<>(firstMapping);
    var variables = builder.after().variables();
    assertThat(variables.size(), equalTo(6));
  }

  @Test
  public void getLiteralsBeforeModification() {

    var builder = new TemplateBuilder<>(firstMapping);
    var literals = builder.before().literals();
    assertThat(literals.size(), equalTo(0));
  }


  @Test
  public void getLiteralsAfterModification() {

    var builder = new TemplateBuilder<>(firstMapping);
    var literals = builder.after().literals();
    System.out.println(literals);
    assertThat(literals.size(), equalTo(5));
  }

  @Test
  public void createWildcardsOfAfterModification() {
    var builder = new TemplateBuilder<>(firstMapping);
    var map = builder.after().createWildcards();

    assertThat(map.entrySet().size(), equalTo(3));
    System.out.println(map);
  }
}