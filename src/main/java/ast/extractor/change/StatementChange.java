package ast.extractor.change;

import java.util.ArrayList;
import java.util.List;

import ast.extractor.Change;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtVariableReference;

public class StatementChange extends Change<CtStatement> {

  private List<CtVariableReference<?>> variables;

  public StatementChange(CtStatement st) {
    super(st);
  }

  @Override
  public List<CtElement> getCompleteChange() {
    var result = new ArrayList<CtElement>();
    this.variables = getVariablesDependencies(element);
    
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
    /*var writeVariables = getWriteVariables(state);
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
    }*/
    var dependencies = this.getVariablesDependencies(state);
    for(var variable: dependencies) {
      if(variables.contains(variable)) {
        return true;
      }
    }

    return false;
  }

  private List<CtVariableReference<?>> getVariablesDependencies(
    CtStatement target) {
      
    return Change.childrenFilter(
      target, 
      CtVariableReference.class,
      item -> item
    );
  }

  /*private List<CtVariableReference<?>> getAccessVariables(CtElement target) {
    return Change.childrenFilter(
      target, 
      CtVariableAccess.class, 
      item -> item.getVariable()
    );
  }*/

  /*
  private List<CtVariableReference<?>> getDefinitionVariables(CtElement target) {
    return Change.childrenFilter(
      target, 
      CtLocalVariable.class, 
      item -> item.getReference()
    );
  }

  private List<CtVariableReference<?>> getReadVariables(CtElement target) {
    return Change.childrenFilter(
      target, 
      CtVariableRead.class, 
      item -> item.getVariable()
    );
  }

  private List<CtVariableReference<?>> getWriteVariables(CtElement target) {
    return Change.childrenFilter(
      target, 
      CtVariableWrite.class, 
      item -> item.getVariable()
    );
  }

  private List<CtVariableReference<?>> getReadFields(CtElement target) {
    return Change.childrenFilter(
      target, 
      CtFieldRead.class,
      item -> item.getVariable()  
    );
  }

  private List<CtVariableReference<?>> getWriteFields(CtElement target) {
    return Change.childrenFilter(
      target, 
      CtFieldWrite.class,
      item -> item.getVariable()  
    );
  }*/
}