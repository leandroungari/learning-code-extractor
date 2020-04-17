package ast;

import static org.junit.Assert.assertTrue;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;

import ast.extractor.ASTComparator;
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
  public void gettingDiffs() {
    var comparator = new ASTComparator(src, dest);
    var result = comparator.mappings();

    assertTrue(result.size() > 0);
  }

  
  
}