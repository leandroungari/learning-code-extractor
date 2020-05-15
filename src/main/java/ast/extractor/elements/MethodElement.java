package ast.extractor.elements;

import java.util.List;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

public class MethodElement extends Element<CtMethod<?>> {

  MethodElement(CtMethod<?> el) {
    super(el);
  }

  @Override
  public List<? extends CtElement> dependencies() {
    return List.of(element);
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