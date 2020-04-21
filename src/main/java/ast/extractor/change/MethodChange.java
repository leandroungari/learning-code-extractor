package ast.extractor.change;

import java.util.List;

import ast.extractor.Change;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

public class MethodChange extends Change<CtMethod<?>> {

  public MethodChange(CtMethod<?> method) {
    super(method);
  }

  @Override
  public List<CtElement> getCompleteChange() {
    return List.of(this.element);
  }

  public List<CtParameter<?>> getParamsFromMethod() {
    
    return get().getParameters();
  }

  public List<CtStatement> getMethodBlock() {

    return get().getBody().getStatements();
  }
}