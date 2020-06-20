package ast;

import org.junit.Test;

public class MappingsExtractorTest {
  
  @Test
  public void commitOfRefactoring() {
    var repository = "refactoring-toy-example";
    var branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    var commit = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
    var builder = new MappingsExtractor(repository, branch, commit);
    var result = builder.createMappings();
    System.out.println(result);
  }

  @Test
  public void commitOfGson() {
    var repository = "gson";
    var branch = "ceae88bd6667f4263bbe02e6b3710b8a683906a2";
    var commit = "157b2323a7e5d9fd925f4dbbac43f67e451985b6";
    
    var builder = new MappingsExtractor(repository, branch, commit);
    var result = builder.createMappings();
    System.out.println(result);
  }

  //478f7f9217cf5da219a7ba298e3a87f405378194 - arquivo duplicados com o mesmo nome
  @Test
  public void testJunit4commit478F7F() {
    var repository = "junit4";
    var branch = "50a285d3ce69b4556ac46d8633f6beb4527b4679";
    var commit = "478f7f9217cf5da219a7ba298e3a87f405378194";
    
    var builder = new MappingsExtractor(repository, branch, commit);
    var result = builder.createMappings();
    System.out.println(result);
  }

  
}