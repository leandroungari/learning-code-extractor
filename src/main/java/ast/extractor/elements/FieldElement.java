package ast.extractor.elements;

import java.util.List;

import spoon.reflect.declaration.CtField;

public class FieldElement extends Element<CtField<?>> {

  FieldElement(CtField<?> el) {
    super(el);
  }

  @Override
  public List<Element<?>> dependencies() {
    return List.of(this);
  }
  
}