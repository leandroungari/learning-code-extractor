package ast.slicing.change;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.CtModel;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

public class StatementChange extends Change<CtStatement> {

  public StatementChange(CtStatement st) {
    super(st);
  }

  @Override
  public List<CtElement> build(CtModel model) {
    var result = new ArrayList<CtElement>();
    var containedVariables = slicer.getVariables(element);

    var block = getParentMethod().getBody();
  
    for(var statement: block) {
      if(slicer.statementSetOneVariables(statement, containedVariables)) {
        result.add(statement);
      }

      if(element.toString().equals(statement.toString())) {
        break;
      };
    }
    return result;
  }
}