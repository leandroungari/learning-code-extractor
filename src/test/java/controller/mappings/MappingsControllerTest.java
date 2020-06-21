package controller.mappings;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

public class MappingsControllerTest {

  @Test
  public void testRefactoring() throws GitAPIException {

    var repository = "refactoring-toy-example";
    var branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    var controller = new MappingsController(repository, branch);
    controller.extractMappingsToStore();
  }

  @Test
  public void testJunit() throws GitAPIException {
    var repository = "junit4";
    var branch = "50a285d3ce69b4556ac46d8633f6beb4527b4679";
    var controller = new MappingsController(repository, branch);
    controller.extractMappingsToStore();
  }

  @Test
  public void testJunitFrom0To480() throws GitAPIException {
    var repository = "junit4";
    var branch = "50a285d3ce69b4556ac46d8633f6beb4527b4679";
    var controller = new MappingsController(repository, branch, 0, 480);
    controller.extractMappingsToStore();
  }

  @Test
  public void testJunitFrom470ToNext() throws GitAPIException {
    var repository = "junit4";
    var branch = "50a285d3ce69b4556ac46d8633f6beb4527b4679";
    var controller = new MappingsController(repository, branch, 470, 1000);
    controller.extractMappingsToStore();
  }

  @Test
  public void testGson() throws GitAPIException {
    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";
    var controller = new MappingsController(repository, branch);
    controller.extractMappingsToStore();
  }

  @Test
  public void testGsonFrom1050() throws GitAPIException {
    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";
    var controller = new MappingsController(repository, branch, 1050, 1484);
    controller.extractMappingsToStore();
  }

  @Test
  public void testGsonFrom1330() throws GitAPIException {
    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";
    var controller = new MappingsController(repository, branch, 1330, 1484);
    controller.extractMappingsToStore();
  }

  @Test
  public void testGsonFrom1430() throws GitAPIException {
    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";
    var controller = new MappingsController(repository, branch, 1430, 1484);
    controller.extractMappingsToStore();
  }

  @Test
  public void testTruthFrom825() throws GitAPIException {
    var repository = "truth";
    var branch = "15453e7104802763fec1770efa13545ee564d526";
    var controller = new MappingsController(repository, branch, 829, 1000);
    controller.extractMappingsToStore();
  }
}