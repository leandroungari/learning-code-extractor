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
    // TODO Auto-generated method stub
    return null;
  }

  public List<CtParameter<?>> getParamsFromMethod() {
    
    return get().getParameters();
  }

  public List<CtStatement> getMethodBlock() {

    return get().getBody().getStatements();
  }
}