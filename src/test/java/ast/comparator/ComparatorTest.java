package ast.comparator;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import ast.extractor.comparator.Comparator;

public class ComparatorTest {
  private static String before = "package org;\n"+
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

    private static String after = "package org;" +
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
    var comparator = new Comparator(before, after);
    var result = comparator.generateMappings();
    /*for(var mapping: result) {
      System.out.println(mapping);
    }*/
    assertThat(result.size(), equalTo(3));
  }
}