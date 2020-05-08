package ast.extractor.elements;

import java.util.List;

import spoon.reflect.declaration.CtConstructor;

public class ConstructorElement extends Element<CtConstructor<?>> {

  ConstructorElement(CtConstructor<?> el) {
    super(el);
  }

  @Override
  public List<Element<?>> dependencies() {
    return List.of(this);
  }
  
}