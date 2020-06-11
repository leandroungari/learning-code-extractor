package ast.extractor.elements;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;

public class FieldElement extends Element<CtField<?>> {

  FieldElement(CtField<?> el) {
    super(el);
    this.priority = 2;
  }

  @Override
  public List<? extends CtElement> dependencies() {
    return List.of(element);
  }

  @Override
  public List<String> extractTokens() {
    var result = new ArrayList<String>();
    element
      .getModifiers()
      .stream()
      .forEach(a -> result.add(a.name()));

    result.add(element.getSimpleName());
    return result;
  }
  
}