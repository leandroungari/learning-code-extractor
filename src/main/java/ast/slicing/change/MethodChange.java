package ast.slicing.change;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

public class MethodChange extends Change<CtMethod<?>> {

  public MethodChange(CtMethod<?> method) {
    super(method);
  }

  @Override
  public List<CtElement> build(CtModel model) {
    
    var result = new ArrayList<CtElement>();

    
    return null;
  }
}