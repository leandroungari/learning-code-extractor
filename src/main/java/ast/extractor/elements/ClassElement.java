package ast.extractor.elements;

import java.util.List;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;

public class ClassElement extends Element<CtClass<?>> {

  public ClassElement(CtClass<?> clazz) {
    super(clazz);
  }

  @Override
  public List<? extends CtElement> dependencies() {
    return List.of(element);
  }

/*  @Override
  protected List<CtElement> getCompleteChange() {
    return List.of(this.element);
  }*/

/*
  public List<CtFieldReference<?>> getAllFields() {

    return element
      .getAllFields()
      .stream()
      .collect(Collectors.toList());
  }

  public List<CtMethod<?>> getAllMethods() {

    return element
      .getAllMethods()
      .stream()
      .collect(Collectors.toList());
  }

  public CtMethod<?> getMethod(String signature) {

    return getAllMethods()
      .stream()
      .filter(method -> method.getSignature().contains(signature))
      .collect(Collectors.toList()).get(0);
  }*/
}