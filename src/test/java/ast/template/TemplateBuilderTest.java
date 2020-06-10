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

  private static Extractor extractor;
  private static CommitMappings mappings;

  private static CtPackage currentRoot;
  private static CtPackage previousRoot;

  private static Snapshot snap;

  @BeforeClass
  public static void init() throws GitAPIException {
    snap = new Snapshot(repository, branch);
  }

  @Test
  public void templateOfMethodUpdate() {
    var commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    extractor = new Extractor(repository, branch, commit);
    mappings = extractor.transform();

    var mapping = mappings.get().get(0);
    var builder = new TemplateBuilder(mapping);
    var template = builder.create();

    currentRoot = snap.commit(commit).root();
    previousRoot = snap.commit(commit).previous().root();
    
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
    var commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    extractor = new Extractor(repository, branch, commit);
    mappings = extractor.transform();

    var mapping = mappings.get().get(1);
    var builder = new TemplateBuilder(mapping);
    var template = builder.create();

    currentRoot = snap.commit(commit).root();
    previousRoot = snap.commit(commit).previous().root();
    
    template.getInput().forEachMatch(previousRoot, item -> {
      System.out.println(item);
    });
    System.out.println("========");
    template.getOutput().forEachMatch(currentRoot, item -> {
      System.out.println(item);
    });
  }

  @Test
  public void verifyPatternOfElement() {
    var commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    extractor = new Extractor(repository, branch, commit);
    mappings = extractor.transform();
    
    var mapping = mappings.get().get(1);
    var builder = new TemplateBuilder(mapping);
    var template = builder.create();

    var result = template.matches(mapping);
    System.out.println("Correspondência: " + result);
  }

  @Test
  public void templateOfClassUpdate() {
    var commit = "c946700398fc43a437d124f027a9bd3ba25bcafc";
    extractor = new Extractor(repository, branch, commit);
    mappings = extractor.transform();

    var mapping = mappings.get().get(3);
    var builder = new TemplateBuilder(mapping);
    var template = builder.create();
    

    currentRoot = snap.commit(commit).root();
    previousRoot = snap.commit(commit).previous().root();
    
    System.out.println(mapping);
    System.out.println(template.getInput().getParameterInfos());
    System.out.println(template.getOutput().getParameterInfos());
    
    var result = template.matches(mapping);
    System.out.println("Resultado: " + result);
  }
}