package ast.extractor.elements;

import java.util.List;

import spoon.reflect.declaration.CtMethod;

public class MethodElement extends Element<CtMethod<?>> {

  MethodElement(CtMethod<?> method) {
    super(method);
  }

  @Override
  public List<Element<?>> dependencies() {
    return List.of(this);
  }

  /*@Override
  protected List<CtElement> getCompleteChange() {
    return List.of(this.element);
  }*/

  /*public List<CtParameter<?>> getParamsFromMethod() {
    
    return get().getParameters();
  }

  public List<CtStatement> getMethodBlock() {

    return get().getBody().getStatements();
  }*/
}