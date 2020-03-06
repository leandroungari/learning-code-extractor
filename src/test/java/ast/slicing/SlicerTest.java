package ast.slicing;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import spoon.Launcher;

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

    var clazz = Launcher.parseClass(CONTENT);
    slicer = new Slicer(clazz);
  }


  @Test
  public void getFields() {

    var result = slicer.getAllFields();

    System.out.println(result.get(0).getSimpleName());

    assertTrue(
      "The number of fields is 1", 
      result.size() == 1
    );
  }

  @Test
  public void getMethods() {

    var result = slicer.getAllMethods();

    for(var method: result) {
      System.out.println(method.getSignature());
    }
  }

  @Test
  public void getVariables() {

    var method = slicer.getMethod("doStuff()");
    var result = slicer.getVariables(method);

    System.out.println("Tamanho: " + result.size());
    for(var item: result) {
      System.out.println(item);
    }
  }


  @Test
  public void getParametersFromMethod() {
    
    System.out.println("=================");
    var methodOne = slicer.getMethod("doStuff()");
    var params1 = slicer.getParamsFromMethod(methodOne);
    System.out.println(params1);

    System.out.println("=================");
    var methodTwo = slicer.getMethod("barkBark");
    var params2 = slicer.getParamsFromMethod(methodTwo);
    System.out.println(params2);
  }

}