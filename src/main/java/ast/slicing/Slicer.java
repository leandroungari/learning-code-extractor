package ast.slicing;

import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class Slicer {

  private CtClass<?> element;

  public Slicer(CtClass<?> clazz) {

    this.element = clazz;
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

  public List<CtVariableReference<?>> getAccessVariables(CtElement e) {

    return e.getElements(new TypeFilter<>(CtVariableAccess.class))
      .stream()
      .map(v -> v.getVariable())
      .collect(Collectors.toList());
  }

  public List<CtVariableReference<?>> getDefinitionVariables(CtElement e) {

    return e.getElements(new TypeFilter<>(CtLocalVariable.class))
      .stream()
      .map(v -> v.getReference())
      .collect(Collectors.toList());
  }

  public List<CtVariableReference<?>> getReadVariables(CtElement e) {

    return e.getElements(new TypeFilter<>(CtVariableRead.class))
      .stream()
      .map(v -> v.getVariable())
      .collect(Collectors.toList());
  }

  public List<CtVariableReference<?>> getWriteVariables(CtElement e) {

    return e.getElements(new TypeFilter<>(CtVariableWrite.class))
      .stream()
      .map(v -> v.getVariable())
      .collect(Collectors.toList());
  }

  public List<CtParameter<?>> getParamsFromMethod(CtMethod<?> method) {

    return method.getParameters();
  }

  public List<CtStatement> getMethodBlock(CtMethod<?> method) {

    return method.getBody().getStatements();
  }

}