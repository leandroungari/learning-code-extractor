package ast.template;

import org.junit.BeforeClass;
import org.junit.Test;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;

public class TemplateTest {

  private static String file;
  private static CtClass<?> clazz;

  @BeforeClass
  public static void before() {
    
    file = "package org;"+
    "import org.animals.Dog;"+
    
    
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

    clazz = Launcher.parseClass(file);
  }

  @Test
  public void verifyTypeOfElement() {

    var template = new Template(clazz);
    System.out.println(template.getType(clazz));
  }

  @Test
  public void extractTree() {

    var template = new Template(clazz);
    var tree = template.extract();

    System.out.println(tree);
  }



}
