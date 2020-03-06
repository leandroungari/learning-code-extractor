package ast.slicing;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import ast.snapshot.Snapshot;
import spoon.Launcher;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;

public class ExtractorTest {

  private static Extractor extractor;

  private static final String ATTRIBUTE_PATH = "#subPackage[name=org]#containedType[name=DogManager]#method[signature=doStuff()]#body#statement[index=0]#target#target";
  private static final String METHOD_PATH = "#subPackage[name=org]#containedType[name=DogManager]#method[signature=barkBark(org.animals.Dog)]";

  private static final String COMMIT = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @BeforeClass
  public static void start() {

    var snapshot = new Snapshot(COMMIT);
    extractor = new Extractor(
      snapshot.model()
    );
  }

  @Test
  public void getElementByPath() {
    
    extractor.getModification(ATTRIBUTE_PATH);
    extractor.getModification(METHOD_PATH);
  }

  @Test
  public void getTypeOfElement() {

    var methodElement = extractor.getModification(METHOD_PATH);
    assertTrue(methodElement instanceof CtMethod);

    var attributeElementCall = extractor.getModification(ATTRIBUTE_PATH);
    System.out.println(attributeElementCall instanceof CtExpression);
    System.out.println(attributeElementCall.getParent().getParent().getParent().getClass());
    //assertTrue(attributeElementCall instanceof CtFieldAccess);
  }
}