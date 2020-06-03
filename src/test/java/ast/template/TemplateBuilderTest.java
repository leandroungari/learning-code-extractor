package ast.template;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;

import ast.Extractor;
import ast.extractor.CommitMappings;
import ast.snapshot.Snapshot;
import spoon.reflect.declaration.CtPackage;

public class TemplateBuilderTest {

  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private static final String commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  private static Extractor extractor;
  private static CommitMappings mappings;

  private static CtPackage currentRoot;
  private static CtPackage previousRoot;

  @BeforeClass
  public static void init() throws GitAPIException {

    extractor = new Extractor(repository, branch, commit);
    mappings = extractor.transform();

    var snap = new Snapshot(repository, branch);
    currentRoot = snap.commit(commit).root();
    previousRoot = snap.commit(commit).previous().root();
  }


  @Test
  public void templateOfMethodUpdate() {
    var mapping = mappings.get().get(0);
    var builder = new TemplateBuilder(mapping);
    var template = builder.create();
    
    template.getInput().forEachMatch(previousRoot, item -> {
      System.out.println(item);
    });
    System.out.println("========");
    template.getOutput().forEachMatch(currentRoot, item -> {
      System.out.println(item);
    });
  }

  @Test
  public void templateOfStatementUpdate() {
    var mapping = mappings.get().get(1);
    var builder = new TemplateBuilder(mapping);
    var template = builder.create();
    
    template.getInput().forEachMatch(previousRoot, item -> {
      System.out.println(item);
    });
    System.out.println("========");
    template.getOutput().forEachMatch(currentRoot, item -> {
      System.out.println(item);
    });
  }
}