package ast.comparator;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import ast.extractor.comparator.Comparator;

public class ComparatorTest {
  private static String beforeA = "package org;\n"+
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

    private static String afterA = "package org;" +
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
  
  @Test
  public void mappingsFromSingleFile() {
    var comparator = new Comparator("antigo.java", beforeA, null, "novo.java", afterA, null);
    var result = comparator.generateMappings();
    System.out.println(result);
    assertThat(result.size(), equalTo(2));
  }

  private static String afterB = "package org.animals;"
    + "public class Dog {"
    + "private int age = 0;"
    + "public int magicNumber = 17;"
  
    + "public int getAge() {"
    +   "return this.age;"
    + "}"
  
    + "public void takeABreath() {"
    +   "System.out.println(\"...\");"
    + "}"
  
  + "}";

  private static String beforeB = "package org.animals;"

  + "import org.DogManager;"
  
  + "public class Dog {"
  
    + "private int age = 0;"
    + "public int magicNumber = 17;"
  
    + "public int getAge() {"
      + "return this.age;"
    + "}"
  
    + "public void barkBark(DogManager manager) {"
      + "System.out.println(\"ruff\");"
      + "System.out.println(\"ruff\");"
      + "takeABreath();"
      + "System.out.println(\"ruff\");"
      + "System.out.println(\"ruff\");"
      + "System.out.println(\"ruff\");"
    + "}"
  
    + "public void takeABreath() {"
      + "System.out.println(\"...\");"
    + "}"
  + "}";

  @Test
  public void mappingsFromSingleFileB() {
    var comparator = new Comparator("antigo.java", beforeB, null, "novo.java", afterB, null);
    var result = comparator.generateMappings();
    System.out.println(result);
    assertThat(result.size(), equalTo(1));
  }
}

