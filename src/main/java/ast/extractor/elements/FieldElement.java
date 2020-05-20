package ast.extractor.elements;

import java.util.List;
import java.util.stream.Collectors;

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
    var items = element
      .getModifiers()
      .stream()
      .map(a -> a.name())
      .collect(Collectors.toList());

    items.add(element.getSimpleName());
    return items;
  }
  
}