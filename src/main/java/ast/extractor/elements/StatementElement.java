package ast.extractor.elements;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class StatementElement extends Element<CtStatement> {

  private List<CtVariableReference<?>> variables;

  public StatementElement(CtStatement st) {
    super(st);
  }

  /*@Override
  protected List<CtElement> getCompleteChange() {
    var result = new ArrayList<CtElement>();
    this.variables = getVariablesDependencies(element);

    var executable = getParentExecutable();
    var block = executable.getBody().getStatements();
    for (var i = block.indexOf(element); i >= 0; i--) {
      if (hasDependency(block.get(i))) {
        result.add(0, block.get(i));
      }
    }

    return result;
  }*/

  @Override
  public List<Element<?>> dependencies() {
    var result = new ArrayList<Element<?>>();
    this.variables = StatementElement.variablesOfStatement(element);

    var executable = parentMethodOrConstructor();
    var block = executable.getBody().getStatements();
    for (var i = block.indexOf(element); i >= 0; i--) {
      if (this.hasDependency(block.get(i))) {
        result.add(0, ElementFactory.create(block.get(i)));
      }
    }

    return result;
  }


  /**
   * It returns the method or constructor which the
   * statement is contained
   * 
   * @return A method or constructor
   */
  private CtExecutable<?> parentMethodOrConstructor() {
    CtExecutable<?> executable = getParentMethod();
    if (executable == null) {
      executable = getParentConstructor();
    }
    return executable;
  }

  /**
   * It identifies the parent method of the statement
   * 
   * @return It returns method element or null
   */
  private CtMethod<?> getParentMethod() {

    return element.getParent(new TypeFilter<>(CtMethod.class));
  }

  /**
   * It return the parent constructor of the statement
   * 
   * @return It return the constructor element or null
   */
  private CtConstructor<?> getParentConstructor() {

    return element.getParent(new TypeFilter<>(CtConstructor.class));
  }

  private boolean hasDependency(CtStatement state) {

    // verifica se alguma variável dependência foi atribuída no comando
    /*
     * var writeVariables = getWriteVariables(state); for(var variable:
     * writeVariables) { if(variables.contains(variable)) { return true; } }
     * //verifica se alguma variável dependência foi definida no comando var
     * definitionVariables = getDefinitionVariables(state); for(var variable:
     * definitionVariables) { if(variables.contains(variable)) { return true; } }
     */
    var dependencies = StatementElement.variablesOfStatement(state);
    for (var variable : dependencies) {
      if (variables.contains(variable)) {
        return true;
      }
    }

    return false;
  }

  private static List<CtVariableReference<?>> variablesOfStatement(CtStatement target) {
    return target.getElements(new TypeFilter<>(CtVariableReference.class));
  }

  /*
   * private List<CtVariableReference<?>> getAccessVariables(CtElement target) {
   * return Change.childrenFilter( target, CtVariableAccess.class, item ->
   * item.getVariable() ); }
   */

  /*
   * private List<CtVariableReference<?>> getDefinitionVariables(CtElement target)
   * { return Change.childrenFilter( target, CtLocalVariable.class, item ->
   * item.getReference() ); }
   * 
   * private List<CtVariableReference<?>> getReadVariables(CtElement target) {
   * return Change.childrenFilter( target, CtVariableRead.class, item ->
   * item.getVariable() ); }
   * 
   * private List<CtVariableReference<?>> getWriteVariables(CtElement target) {
   * return Change.childrenFilter( target, CtVariableWrite.class, item ->
   * item.getVariable() ); }
   * 
   * private List<CtVariableReference<?>> getReadFields(CtElement target) { return
   * Change.childrenFilter( target, CtFieldRead.class, item -> item.getVariable()
   * ); }
   * 
   * private List<CtVariableReference<?>> getWriteFields(CtElement target) {
   * return Change.childrenFilter( target, CtFieldWrite.class, item ->
   * item.getVariable() ); }
   */
}