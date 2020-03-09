package ast.slicing.change;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.CtModel;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtElement;

public class StatementChange extends Change<CtStatement> {

  private List<CtVariableAccess<?>> variables;

  public StatementChange(CtStatement st) {
    super(st);
  }

  @Override
  public List<CtElement> build(CtModel model) {
    var result = new ArrayList<CtElement>();
    this.variables = slicer.getAccessVariables(element);
    
    var block = slicer.getMethodBlock(getParentMethod());
    for(var i = block.indexOf(element); i >= 0; i--) {
      System.out.println(block.get(i));
      if(hasDependency(block.get(i))) {
        result.add(0, block.get(i));
      }

    }


    return result;
  }

  private boolean hasDependency(CtStatement state) {
    var writeVariables = slicer.getWriteVariables(state);
    for(var variable: writeVariables) {
      if(variables.contains(variable)) {
        return true;
      }
    }
    var localVariables = slicer.getDefinitionVariables(state);
    for(var variable: localVariables) {
      if(variables.contains(variable.getReference())) {
        return true;
      }
    }
    return false;
  }
}