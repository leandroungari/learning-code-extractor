package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.extractor.mapping.Mapping;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtVariableReference;

public class TemplateBuilder<T extends Mapping> {

  private List<? extends CtElement> sources;
  private List<? extends CtElement> destinations;

  private List<CtVariableReference<?>> variables;
  private List<CtLiteral<?>> literals;

  private Map<String,String> wildcards;

  public TemplateBuilder(T mapping) {
    
    this.variables = this.extractVariablesFromElement(mapping);
    this.literals = this.extractLiteralsFromElement(mapping);

    if(mapping.source() != null) {
      this.sources = mapping.source().dependencies();
    } else {
      this.sources = new ArrayList<>();
    }
    
    if(mapping.destination() != null) {
      this.destinations = mapping.destination().dependencies();
    } else {
      this.destinations = new ArrayList<>();
    }
  }

  private List<CtVariableReference<?>> extractVariablesFromElement(T item) {

    List<CtVariableReference<?>> before = null, after = null;
    if(item.source() != null) {
      before = item.source().getVariables();
    } else {
      before = new ArrayList<>();
    }

    if(item.destination() != null) {
      after = item.destination().getVariables();
    } else {
      after = new ArrayList<>();
    }

    return Stream
      .concat(before.stream(), after.stream())
      .collect(Collectors.toList());
  }

  private List<CtLiteral<?>> extractLiteralsFromElement(T item) {
    List<CtLiteral<?>> before = null, after = null;

    if(item.source() != null) {
      before = item.source().getLiterals();
    } else {
      before = new ArrayList<>();
    }
    
    if(item.destination() != null) {
      after = item.destination().getLiterals();
    } else {
      after = new ArrayList<>();
    }
    
    return Stream
      .concat(before.stream(), after.stream())
      .collect(Collectors.toList());
  }

  private void replaceVariable(CtVariableReference<?> variable) {
    if(variable.getDeclaration() != null) {
      variable
        .getDeclaration()
        .setSimpleName(wildcards.get(variable.prettyprint()));    
    }

    variable.setSimpleName(wildcards.get(variable.prettyprint()));
  }

  private void replaceLiteral(CtLiteral<?> literal) {

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

  public Pattern model() {    
    this.wildcards = createWildcards();
    for(var variable: variables) {
      this.replaceVariable(variable);
    }
    for(var literal: literals) {
      this.replaceLiteral(literal);
    }

    var sourceItems = sources
      .stream()
      .map(item -> item.toString())
      .collect(Collectors.toList());

    var destinationItems = destinations
      .stream()
      .map(item -> item.toString())
      .collect(Collectors.toList());
    
    return new Pattern(
      sourceItems.toString(),
      destinationItems.toString()
    );
  }

  public Map<String, String> createWildcards() {
    var wildcards = new HashMap<String, String>();
    var counter = 0;
    for (var variable : this.variables) {
      if(!wildcards.containsKey(variable.prettyprint())) {
        wildcards.put(
          variable.prettyprint(),
          "$v" + counter++
        );
      }
    }
    for (var literal : this.literals) {
      if(!wildcards.containsKey(literal.prettyprint())) {
        wildcards.put(
          literal.prettyprint(), 
          "$l" + counter++
        );
      }
    }
    return wildcards;
  }
}