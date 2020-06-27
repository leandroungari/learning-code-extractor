package controller.mappings;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

public class ClusteringControllerTest {
  

  @Test
  public void testRefactoring() throws GitAPIException {

    var repository = "refactoring-toy-example";
    var branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    var controller = new ClusteringController(repository, branch);
    controller.clusterToStore();
  }

  @Test
  public void gsonFrom475To502() throws GitAPIException {

    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";
    var controller = new ClusteringController(repository, branch, 475, 502);
    controller.clusterToStore();
  }
}