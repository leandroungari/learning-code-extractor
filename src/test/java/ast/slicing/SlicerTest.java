package ast.slicing;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class SlicerTest {

  private static final String CONTENT = 
    "package org;" +
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

  private static Slicer slicer;

  @BeforeClass
  public static void start() {

    slicer = new Slicer(CONTENT);
  }


  @Test
  public void getNameOfFields() {

    var result = slicer.getAllFields();

    assertTrue(
      "The number of fields is 1", 
      result.size() == 1
    );
  }

  

  private
}