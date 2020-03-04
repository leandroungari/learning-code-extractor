package ast.slicing;

import java.util.List;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.path.CtPathStringBuilder;

public class Extractor {

  private CtClass<?> clazz;

  public Extractor(CtClass<?> clazz) {
    this.clazz = clazz;
  }

  public void getModification(String path) {

    clazz.
  }

  private List<CtElement> getElementByPath(String path) {

    var builder = new CtPathStringBuilder()
      .fromString(path);
    
    return builder.evaluateOn(clazz);
  }


}