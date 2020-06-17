package controller.mappings;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

public class MappingsControllerTest {

  @Test
  public void entireBranch() throws GitAPIException {

    var repository = "refactoring-toy-example";
    var branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    var controller = new MappingsController(repository, branch);
    controller.extractMappingsToStore();
  }
}