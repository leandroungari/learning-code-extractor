package ast;

import java.util.ArrayList;
import java.util.List;

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
    for(var change: changes) {
      for(var variable: change.getVariablesDependencies()) {
        result.add(variable);
      }
    }
    return result;
  }

  public List<CtLiteral<?>> literals() {
    var result = new ArrayList<CtLiteral<?>>();
    for(var change: changes) {
      for(var literal: change.getLiterals()) {
        result.add(literal);
      }
    }
    return result;
  }
}