package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ast.extractor.Change;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.reference.CtVariableReference;

public class ProcessorTemplate {

  private List<Change<?>> changes;
  private Map<String, String> wildcards;

  ProcessorTemplate(List<Change<?>> changes) {
    this.changes = changes;
  }

  public List<CtVariableReference<?>> variables() {
    var result = new ArrayList<CtVariableReference<?>>();
    for (var change : changes) {
      for (var variable : change.getVariablesDependencies()) {
        result.add(variable);
      }
    }
    return result;
  }

  public List<CtLiteral<?>> literals() {
    var result = new ArrayList<CtLiteral<?>>();
    for (var change : changes) {
      for (var literal : change.getLiterals()) {
        result.add(literal);
      }
    }
    return result;
  }

  public Map<String, String> createWildcards() {
    var wildcards = new HashMap<String, String>();
    var counter = 0;
    for (var variable : variables()) {
      if(!wildcards.containsKey(variable.prettyprint())) {
        wildcards.put(
          variable.prettyprint(),
          "$v" + counter++
        );
      }
    }
    for (var literal : literals()) {
      if(!wildcards.containsKey(literal.prettyprint())) {
        wildcards.put(
          literal.prettyprint(), 
          "$l" + counter++
        );
      }
    }
    return wildcards;
  }

  private void replaceVariablesName(
    List<CtVariableReference<?>> variables) {

    for(var variable: variables) {
      if(variable.getDeclaration() != null) {
        variable
          .getDeclaration()
          .setSimpleName(wildcards.get(variable.prettyprint()));
      } else {
        variable.setSimpleName(wildcards.get(variable.prettyprint()));
      }
    }
  }

  private void createVariableToReplaceLiteral(
    CtLiteral<?> literal) {
    
    var myVariable = literal.getFactory().Code()
      .createLocalVariable(
        literal.getType(), 
        wildcards.get(literal.prettyprint()), 
        null
      );

    try {
      var referenceToVariable = literal.getFactory().Code()
        .createVariableRead(myVariable.getReference(), false);

      literal.replace(referenceToVariable);
    } catch (Exception ex) {
      System.err.println(ex);

      var referenceToVariable = literal.getFactory().Code()
        .createVariableWrite(myVariable.getReference(), false);

      literal.replace(referenceToVariable);
    }
  }

  public List<Change<?>> replaceElementsByWildcards(
    Map<String, String> wildcards) {
    
    this.wildcards = wildcards;
    var variables = new ArrayList<CtVariableReference<?>>();
    for(var change: changes) {
      for(var variable: change.getVariablesDependencies()) {
        variables.add(variable);  
      }
      replaceVariablesName(variables);

      for(var literal: change.getLiterals()) {
        createVariableToReplaceLiteral(literal);
      }
    }

    return changes;
  }

  public List<String> model() {
    var wildcards = createWildcards();
    return replaceElementsByWildcards(wildcards)
      .stream()
      .map(item -> item.get().prettyprint())
      .collect(Collectors.toList());
  }
}