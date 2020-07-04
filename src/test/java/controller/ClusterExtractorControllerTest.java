package controller;

import org.junit.Test;

public class ClusterExtractorControllerTest {
  
  @Test
  public void repoGson() {
    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";
    var controller = new ClusterExtractorController(repository, branch);
    controller.execute();
  }
}