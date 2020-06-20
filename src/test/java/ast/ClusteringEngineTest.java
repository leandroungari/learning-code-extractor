package ast;

import org.junit.Test;

public class ClusteringEngineTest {

  @Test
  public void testRefactoring() {
    var repository = "refactoring-toy-example";
    var branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    
    var engine = new ClusteringEngine(repository, branch);
    engine.generate();
  }

  @Test
  public void testJunit() {
    var repository = "junit4";
    var branch = "50a285d3ce69b4556ac46d8633f6beb4527b4679";
    
    var engine = new ClusteringEngine(repository, branch);
    engine.generate();
  }

  @Test
  public void testGson() {
    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";
    
    var engine = new ClusteringEngine(repository, branch);
    engine.generate();
  }

  @Test
  public void testGsonFrom30To100() {
    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";
    
    var engine = new ClusteringEngine(repository, branch, 30, 100);
    engine.generate();
  }
}