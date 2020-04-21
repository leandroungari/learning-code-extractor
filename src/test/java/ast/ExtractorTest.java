package ast;

import org.junit.BeforeClass;
import org.junit.Test;

import ast.extractor.Change;
import ast.extractor.change.StatementChange;
import ast.extractor.mapping.DeleteMapping;
import ast.extractor.mapping.InsertMapping;
import ast.extractor.mapping.Mapping;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;

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

  @Test
  public void extractCompleteStatementModifications() {

    var modifications = extractor.transformModifications();
    for (var modification : modifications.getModifications()) {
      System.out.println("================");
      System.out.println("oldPath: " + modification.getOldPath());
      System.out.println("newPath: " + modification.getNewPath());
      for (var mapping : modification.getMappings()) {
        var source = extractSource(mapping);
        var destination = extractDestination(mapping);
        // extraindo as modificações completas dos statements
        System.out.println("---------------");
        if (source != null && source instanceof StatementChange) {
          System.out.println(source.getCompleteChange());
        }
        System.out.println(">>>>>>>>>>>>>>>");
        if (destination != null && destination instanceof StatementChange) {
          System.out.println(destination.getCompleteChange());
        }
        System.out.println("+++++++++++++++");
      }
    }
  }

  private Change<?> extractSource(Mapping mapping) {
    Change<?> change = null;
    if (mapping instanceof DeleteMapping) {
      var m = (DeleteMapping) mapping;
      change = m.source();
    } else if (mapping instanceof UpdateMapping) {
      var m = (UpdateMapping) mapping;
      change = m.source();
    } else if (mapping instanceof MoveMapping) {
      var m = (MoveMapping) mapping;
      change = m.source();
    }

    return change;
  }

  private Change<?> extractDestination(Mapping mapping) {
    Change<?> change = null;
    if (mapping instanceof InsertMapping) {
      var m = (InsertMapping) mapping;
      change = m.destination();
    } else if (mapping instanceof UpdateMapping) {
      var m = (UpdateMapping) mapping;
      change = m.destination();
    } else if (mapping instanceof MoveMapping) {
      var m = (MoveMapping) mapping;
      change = m.destination();
    }

    return change;
  }
}