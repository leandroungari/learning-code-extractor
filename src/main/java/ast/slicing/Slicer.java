package ast.slicing;

import java.util.List;
import java.util.stream.Collectors;

import spoon.Launcher;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class Slicer {

  private CtClass<?> element;

  public Slicer(CtClass<?> clazz) {

    this.element = clazz;
  }


  public List<CtFieldReference<?>> getAllFields() {

    return element.getAllFields().stream().collect(Collectors.toList());
  }

  public List<CtMethod<?>> getAllMethods() {

    return element.getAllMethods().stream().collect(Collectors.toList());
  }

  public CtMethod<?> getMethod(String signature) {

    return getAllMethods().stream().filter(method -> method.getSignature().contains(signature))
        .collect(Collectors.toList()).get(0);
  }

  public List<CtVariableAccess<?>> getAccessVariables(CtElement element) {

    return element.getElements(new TypeFilter<>(CtVariableAccess.class));
  }

  public List<CtVariableWrite<?>> getWriteVariables(CtElement element) {

    return element.getElements(new TypeFilter<>(CtVariableWrite.class));
  }

  public List<CtParameter<?>> getParamsFromMethod(CtMethod<?> method) {

    return method.getParameters();
  }


}