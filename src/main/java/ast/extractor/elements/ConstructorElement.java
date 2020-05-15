package ast.extractor.elements;

import java.util.List;

import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;

public class ConstructorElement extends Element<CtConstructor<?>> {

  ConstructorElement(CtConstructor<?> el) {
    super(el);
  }

  @Override
  public List<? extends CtElement> dependencies() {
    return List.of(element);
  }
  
}