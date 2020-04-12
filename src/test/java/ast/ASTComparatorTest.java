package ast;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;

import repository.exception.GitRepositoryException;

public class ASTComparatorTest {

  private static String src;
  private static String dest;

  @BeforeClass
  public static void start() throws GitRepositoryException, GitAPIException {

    var contentA = "package org;\n"+
    "import org.animals.Dog;\n"+
    
    
    "public class DogManager {" +
    
      "private Dog dog;" +
      "public DogManager(Dog aDog) {" +
        "this.dog = aDog;" +
      "}"+
      
      "public void doStuff() {" +
        "this.dog.barkBark(this);" +
        
        "int age = dog.getAge();" +
        "int sum = 0;" +
        "for (int i = 0; i < age; i++) {"+
          "System.out.println(i);" +
          "sum += i;" +
        "}" +
        "sum -= dog.magicNumber;" +
        "dog.takeABreath();" +
      "}" +
    "}";

    var contentB = "package org;" +
    "import org.animals.Dog;" +
    
    
    "public class DogManager {" +
    
      "private Dog dog;" +
      "public DogManager(Dog aDog) {" +
        "this.dog = aDog;" +
      "}" +
      
      "public void doStuff() {" +
        "barkBark(this.dog);" +
        
        "int age = dog.getAge();" +
        "int sum = 0;" +
        "for (int i = 0; i < age; i++) {" +
          "System.out.println(i);" +
          "sum += i;" +
        "}" +
        "sum -= dog.magicNumber;" +
        "dog.takeABreath();" +
      "}" +
    
      "public void barkBark(Dog dog) {" +
        "System.out.println(\"ruff\");" +
        "System.out.println(\"ruff\");" +
        "dog.takeABreath();" +
        "System.out.println(\"ruff\");" +
        "System.out.println(\"ruff\");" +
        "System.out.println(\"ruff\");" +
      "}"+
    "}";

    src = contentA;
    dest = contentB;
  }

  @Test
  public void obtainingMappings() throws GitAPIException {

    var comparator = new ASTComparator(src, dest);
    var mappings = comparator.mappings();

    System.out.println("Quantidade de mapeamentos: " + mappings.size());
    mappings.forEach(a -> {
      System.out.println("=====================");
      System.out.println(a.type());
      System.out.println(a.source());
      System.out.println(a.destination());
    });
  }

  @Test
  public void extractModifications() {
    var comparator = new ASTComparator(src, dest);
    for(var modification: comparator.extract()) {
      System.out.println(modification);
    }
  }

  @Test
  public void listChildrenOfModifications() {
    var comparator = new ASTComparator(src, dest);
    for(var modification: comparator.extract()) {
      System.out.println(">>>>>>>>>>>>>>>>>");
      for(var mapping: modification) {
        
        if(mapping.type() == OperationType.INSERT) {
          System.out.println("+++++");
          mapping.destination().asIterable().forEach(System.out::println);
        }
        else if(mapping.type() == OperationType.DELETE) {
          System.out.println("-----");
          mapping.source().asIterable().forEach(System.out::println);
        }
        else {
          System.out.println("-----");
          mapping.source().asIterable().forEach(System.out::println);
          System.out.println("+++++");
          mapping.destination().asIterable().forEach(System.out::println);
        }
      }
    }
    
  }
  
  
}