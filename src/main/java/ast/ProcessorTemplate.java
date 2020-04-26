package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ast.extractor.Change;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.reference.CtVariableReference;

public class ProcessorTemplate {

  private List<Change<?>> changes;

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
      var name = variable.getSimpleName();
      if(!wildcards.containsKey(name)) {
        wildcards.put(
          variable.getSimpleName(),
          "$v" + counter++
        );
      }
    }
    for (var literal : literals()) {
      var name = literal.getValue().toString();
      if(!wildcards.containsKey(name)) {
        wildcards.put(
          name, 
          "$l" + counter++
        );
      }
    }
    return wildcards;
  }
}