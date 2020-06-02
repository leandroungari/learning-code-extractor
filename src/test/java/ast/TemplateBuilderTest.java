package ast;

import org.junit.BeforeClass;
import org.junit.Test;

import ast.extractor.CommitMappings;
import spoon.pattern.PatternBuilder;

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

  @Test
  public void generatePattern() {
    var item = mappings.get().get(0);
    //var builder = new TemplateBuilder<>(mappings.get().get(0));
    //var result = builder.model();
    
    var pattern = PatternBuilder
      //.create(result.destination())
      .create(item.destination().get())
      .configurePatternParameters(pb -> {
        pb.parameter("param").byNamedElement("dog");
        pb.parameter("literal").byString("ruff");
        pb.parameter("methodName").byString("barkBark");
      })
      .build();

    pattern.forEachMatch(item.destination().get(), match -> {
      System.out.println(match.getParametersMap());
    });

    System.out.println(item.destination().get());
    
    //System.out.println(pattern.print(false));
    
  }
}