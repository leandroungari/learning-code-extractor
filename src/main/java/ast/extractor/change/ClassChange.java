package ast.extractor.change;

import java.util.List;
import java.util.stream.Collectors;

import ast.extractor.Change;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtFieldReference;

public class ClassChange extends Change<CtClass<?>> {

  public ClassChange(CtClass<?> clazz) {
    super(clazz);
  }

  @Override
  protected List<CtElement> getCompleteChange() {
    return List.of(this.element);
  }


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
  }
}