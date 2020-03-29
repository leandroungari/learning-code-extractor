package ast.slicing.change;

import java.util.List;

import spoon.reflect.CtModel;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

public class MethodChange extends Change<CtMethod<?>> {

  public MethodChange(CtMethod<?> method) {
    super(method);
  }

  @Override
  public List<CtElement> build(CtModel model) {
    
    //var result = new ArrayList<CtElement>();

    
    return null;
  }

  public List<CtParameter<?>> getParamsFromMethod() {
    
    return get().getParameters();
  }

  public List<CtStatement> getMethodBlock() {

    return get().getBody().getStatements();
  }
}