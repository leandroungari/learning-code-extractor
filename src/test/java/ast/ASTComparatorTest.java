package ast;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.BeforeClass;
import org.junit.Test;

import repository.exception.GitRepositoryException;
import spoon.reflect.path.CtRole;
import spoon.support.reflect.declaration.CtMethodImpl;

public class ASTComparatorTest {

  private static SourceCodeFile src;
  private static SourceCodeFile dest;

  private static final String FILE = "src/DogManager.java";


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

    src = SourceCodeFile.create(FILE, contentA);
    dest = SourceCodeFile.create(FILE, contentB);
  }

  @Test
  public void obtainingMappings() throws GitAPIException {

    var comparator = new ASTComparator(src, dest);
    var mappings = comparator.mappings();

    System.out.println("Quantidade de mapeamentos: " + mappings.size());
    mappings.forEach(a -> {
      System.out.println("=====================");
      System.out.println(a.type());
      System.out.println(a.source().get());
      System.out.println(a.destination().get());
    });

  }

  @Test
  public void generateScript() {

    var comparator = new ASTComparator(src, dest);
    var list = comparator.editScript();

    list.forEach(a -> {
      System.out.println("=====================");
      System.out.println(a.getAction().getName());
      System.out.println(a.getSrcNode());
      System.out.println(a.getDstNode());
    });
  }

  @Test
  public void walkingInTree() {

    var comparator = new ASTComparator(src, dest);
    var element = comparator.editScript().get(0);

    element.getSrcNode().asIterable().forEach(e -> {
      System.out.println(e.getShortRepresentation());
    });
  }

  @Test 
  public void verifyTypeOfElement() {

    var comparator = new ASTComparator(src, dest);
    var element = comparator.editScript().get(0);

    var node = element.getSrcNode();
    System.out.println(node.getShortRepresentation());
    System.out.println(node instanceof CtMethodImpl);
  }

  @Test
  public void transversingChildrenElement() {

    var comparator = new ASTComparator(src, dest);
    var element = comparator.editScript().get(0);

    var node = element.getSrcNode();

    node.descendantIterator().forEachRemaining(e -> {
      System.out.println("=====================");
      System.out.println(e.getPath());
      System.out.println(e.getShortRepresentation());
    
    });
  }

  @Test
  public void handleMethodDeclaration() {

    var comparator = new ASTComparator(src, dest);
    var element = comparator.editScript().get(0);

    var node = element.getSrcNode().getRoleInParent();

    System.out.println(node.getCamelCaseName());
  }

}