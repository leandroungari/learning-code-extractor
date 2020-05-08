package ast.extractor.elements;

import java.util.List;

import spoon.reflect.declaration.CtInterface;

public class InterfaceElement extends Element<CtInterface<?>> {

  InterfaceElement(CtInterface<?> el) {
    super(el);
  }

  @Override
  public List<Element<?>> dependencies() {
    return List.of(this);
  }
  
}