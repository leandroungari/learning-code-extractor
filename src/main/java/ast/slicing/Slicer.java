package ast.slicing;

import java.util.List;
import java.util.stream.Collectors;

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

  public List<CtVariableAccess<?>> getVariables(CtElement e) {

    return e.getElements(new TypeFilter<>(CtVariableAccess.class));
  }

  public List<CtVariableRead<?>> getReadVariables(CtElement e) {

    return e.getElements(new TypeFilter<>(CtVariableRead.class));
  }

  public List<CtVariableWrite<?>> getWriteVariables(CtElement e) {

    return e.getElements(new TypeFilter<>(CtVariableWrite.class));
  }

  public List<CtParameter<?>> getParamsFromMethod(CtMethod<?> method) {

    return method.getParameters();
  }

  public boolean statementSetOneVariables(CtStatement statement, List<CtVariableAccess<?>> list) {
    
    var contains = false;
    var listVariables = getVariables(statement);
    System.out.println("1 - " + statement);
    var variable = (listVariables.size() > 0 ? listVariables.get(0) : null);
    if(variable != null) {
      for(var access: list) {
        System.out.println(access.getVariable().getSimpleName() + " -- " + variable.getVariable().getSimpleName());
        if(access == variable) {
          contains = true;
        } 
      }
    }
    
    return contains;
  }

}