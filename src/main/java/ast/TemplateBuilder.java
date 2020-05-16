package ast;

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

    this.sources = mapping.source().dependencies();
    this.destinations = mapping.destination().dependencies();
  }

  private List<CtVariableReference<?>> extractVariablesFromElement(T item) {
    var before = item.source().getVariables();
    var after = item.destination().getVariables();
    
    return Stream
      .concat(before.stream(), after.stream())
      .collect(Collectors.toList());
  }

  private List<CtLiteral<?>> extractLiteralsFromElement(T item) {
    var before = item.source().getLiterals();
    var after = item.destination().getLiterals();
    
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

  public Map<List<String>,List<String>> model() {    
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
    
    
    return Map.of(sourceItems, destinationItems);
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