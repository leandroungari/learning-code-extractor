package ast.extractor.elements;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class StatementElement extends Element<CtStatement> {

  public StatementElement(CtStatement el) {
    super(el);
    this.priority = 3;
  }

  @Override
  public List<? extends CtElement> dependencies() {
    var result = new ArrayList<CtElement>();
    var variables = StatementElement.variablesOfStatement(element);

    var executable = parentMethodOrConstructor();
    if(executable != null && executable.getBody() != null) {
      var block = executable.getBody().getStatements();
      for (var i = block.indexOf(element); i >= 0; i--) {
        if (this.hasDependency(block.get(i), variables)) {
          result.add(0, block.get(i));
        }
      }
    }

    if(result.isEmpty()) {
      result.add(this.get());
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
   * It identifies the parent method of the statement. 
   * It gets the parent based on the first element of dependencies
   * 
   * @return It returns method element or null
   */
  private CtMethod<?> getParentMethod() {

    return element.getParent(new TypeFilter<>(CtMethod.class));
  }

  /**
   * It return the parent constructor of the statement
   * It gets the parent based on the first element of dependencies
   * 
   * @return It return the constructor element or null
   */
  private CtConstructor<?> getParentConstructor() {

    return element.getParent(new TypeFilter<>(CtConstructor.class));
  }

  private boolean hasDependency(
    CtStatement state, 
    List<CtVariableReference<?>> variables) {

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

  @Override
  public List<String> extractTokens() {
    var result = new ArrayList<String>();
    result.add(element.toString());
    return result;
  }
}