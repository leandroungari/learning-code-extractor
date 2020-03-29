package ast.slicing.change;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.CtModel;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class StatementChange extends Change<CtStatement> {

  private List<CtVariableReference<?>> variables;

  public StatementChange(CtStatement st) {
    super(st);
  }

  @Override
  public List<CtElement> build(CtModel model) {
    var result = new ArrayList<CtElement>();
    this.variables = getAccessVariables(element);
    
    var block = getParentMethod().getBody().getStatements();
    for(var i = block.indexOf(element); i >= 0; i--) {
      if(hasDependency(block.get(i))) {
        result.add(0, block.get(i));
      }
    }

    return result;
  }

  private boolean hasDependency(CtStatement state) {
    //verifica se alguma variável dependência foi atribuída no comando
    var writeVariables = getWriteVariables(state);
    for(var variable: writeVariables) {
      if(variables.contains(variable)) {
        return true;
      }
    }
    //verifica se alguma variável dependência foi definida no comando
    var definitionVariables = getDefinitionVariables(state);
    for(var variable: definitionVariables) {
      if(variables.contains(variable)) {
        return true;
      }
    }
    return false;
  }

  private List<CtVariableReference<?>> getAccessVariables(CtElement e) {

    return e.getElements(new TypeFilter<>(CtVariableAccess.class))
      .stream()
      .map(v -> v.getVariable())
      .collect(Collectors.toList());
  }

  private List<CtVariableReference<?>> getDefinitionVariables(CtElement e) {

    return e.getElements(new TypeFilter<>(CtLocalVariable.class))
      .stream()
      .map(v -> v.getReference())
      .collect(Collectors.toList());
  }

  private List<CtVariableReference<?>> getReadVariables(CtElement e) {

    return e.getElements(new TypeFilter<>(CtVariableRead.class))
      .stream()
      .map(v -> v.getVariable())
      .collect(Collectors.toList());
  }

  private List<CtVariableReference<?>> getWriteVariables(CtElement e) {

    return e.getElements(new TypeFilter<>(CtVariableWrite.class))
      .stream()
      .map(v -> v.getVariable())
      .collect(Collectors.toList());
  }
}