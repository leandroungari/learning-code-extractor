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

    var containedVariables = slicer.get

    var block = getParentMethod().getBody();
    block.forEach(statement -> {

    });
    
    return result;
  }
}