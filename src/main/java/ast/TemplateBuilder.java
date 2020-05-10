package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.extractor.elements.Element;
import ast.extractor.mapping.Mapping;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.reference.CtVariableReference;

public class TemplateBuilder<T extends Mapping> {

  private List<Element<?>> source;
  private List<Element<?>> destination;

  private List<CtVariableReference<?>> variables;
  private List<CtLiteral<?>> literals;

  private Map<String,String> wildcards;

  public TemplateBuilder(T mapping) {

    this.source = TemplateBuilder.createList(mapping.source());
    this.destination = TemplateBuilder.createList(mapping.destination());
  }

  private static List<Element<?>> createList(Element<?> e) {
    if(e == null) return new ArrayList<Element<?>>();
    return e.dependencies();
  }

  private static List<CtVariableReference<?>> variables(
    List<Element<?>> elements) {
  
    var result = new ArrayList<CtVariableReference<?>>();
    for (var item : elements) {
      for (var variable : item.getVariables()) {
        result.add(variable);
      }
    }
    return result;
  }

  private static List<CtLiteral<?>> literals(
    List<Element<?>> elements) {

    var result = new ArrayList<CtLiteral<?>>();
    for (var item : elements) {
      for (var literal : item.getLiterals()) {
        result.add(literal);
      }
    }
    return result;
  }

  private List<CtVariableReference<?>> extractVariablesFromElement() {
    var before = variables(this.source);
    var after = variables(this.destination);
    
    return Stream
      .concat(before.stream(), after.stream())
      .collect(Collectors.toList());
  }

  private List<CtLiteral<?>> extractLiteralsFromElement() {
    var before = literals(source);
    var after = literals(destination);
    
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
    this.variables = this.extractVariablesFromElement();
    this.literals = this.extractLiteralsFromElement();
    
    this.wildcards = createWildcards();
    for(var variable: variables) {
      this.replaceVariable(variable);
    }
    for(var literal: literals) {
      this.replaceLiteral(literal);
    }

    var sourceItems = source
      .stream()
      .map(item -> item.get().toString())
      .collect(Collectors.toList());

    var destinationItems = destination
      .stream()
      .map(item -> item.get().toString())
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