package controller;

import org.junit.Test;

public class ClusterExtractorControllerTest {
  
  @Test
  public void repoRefactoring() {
    ////o cluster está com snapshot null
    var repository = "refactoring-toy-example";
    var branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    var controller = new ClusterExtractorController(repository, branch);
    controller.execute();
  }

  @Test
  public void repoGson() {
    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";
    var controller = new ClusterExtractorController(repository, branch);
    controller.execute();
  }
}