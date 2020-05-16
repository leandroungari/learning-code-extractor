package ast.extractor.elements;

import java.util.List;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;

public class InterfaceElement extends Element<CtInterface<?>> {

  InterfaceElement(CtInterface<?> el) {
    super(el);
    this.priority = 1;
  }

  @Override
  public List<? extends CtElement> dependencies() {
    return List.of(element);
  }
  
}