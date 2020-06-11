package ast.template;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.extractor.mapping.Mapping;
import ast.filter.FieldFilter;
import ast.filter.LiteralFilter;
import ast.filter.LocalVariableFilter;
import ast.filter.MethodFilter;
import ast.filter.TypeReferenceFilter;
import ast.filter.VariableReferenceFilter;
import spoon.pattern.ConflictResolutionMode;
import spoon.pattern.Pattern;
import spoon.pattern.PatternBuilder;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class TemplateBuilder {

  private List<? extends CtElement> sources;
  private List<? extends CtElement> destinations;

  private List<CtVariableReference<?>> variables;
  private List<CtLiteral<?>> literals;
  private List<CtMethod<?>> methods;
  private List<CtField<?>> fields;
  private List<CtLocalVariable<?>> localVariables;
  private List<CtTypeReference<?>> types;

  public TemplateBuilder(Mapping mapping) {

    if (mapping.source() != null) {
      this.sources = mapping.source().dependencies();
    } else {
      this.sources = new ArrayList<>();
    }

    if (mapping.destination() != null) {
      this.destinations = mapping.destination().dependencies();
    } else {
      this.destinations = new ArrayList<>();
    }
  }

  private void extractInfo() {
    var elements = Stream
      .concat(this.sources.stream(), this.destinations.stream())
      .collect(Collectors.toList());
      
    var variablesFilter = new VariableReferenceFilter(elements);
    this.variables = variablesFilter.filter();

    var literalsFilter = new LiteralFilter(elements);
    this.literals = literalsFilter.filter();

    var methodsFilter = new MethodFilter(elements);
    this.methods = methodsFilter.filter();

    var fieldsFilter = new FieldFilter(elements);
    this.fields = fieldsFilter.filter();

    var localVariablesFilter = new LocalVariableFilter(elements);
    this.localVariables = localVariablesFilter.filter();

    var typeFilter = new TypeReferenceFilter(elements);
    this.types = typeFilter.filter();
  }

  public Template create() {
    this.extractInfo();
    var source = this.manipulate(this.sources);
    var destination = this.manipulate(this.destinations);
    return new Template(source, destination);
  }

  private Pattern manipulate(List<? extends CtElement> items) {
    return PatternBuilder
      .create(items)
      .configurePatternParameters(pb -> {
        pb.setConflictResolutionMode(ConflictResolutionMode.USE_NEW_NODE);
        for(var type: this.types) {    
          try{
            pb.parameter(type.getQualifiedName())
              .byType(type);
          } catch(Exception e) {
            System.err.println(this.types);
            System.err.println(e.getMessage());
            //throw new Error("Type Filter Error");
          }
        }
        for(var reference: this.variables) {  
          pb.parameter(reference.getSimpleName())
            .byString(reference.getSimpleName());
        }
        for(var localVariable: this.localVariables) {
          pb.parameter(localVariable.getSimpleName())
            .byString(localVariable.getSimpleName());
        }
        for(var literal: this.literals) {
          pb.parameter(literal.toString())
            .byString(literal.toString());
        }
        for(var method: this.methods) {
          pb.parameter(method.getSignature())
            .byString(method.getSimpleName());
        }
        for(var field: this.fields) {
          pb.parameter(field.getSimpleName())
            .byString(field.getSimpleName());
        }

        pb.parameter("modifier-class")
          .byRole(CtRole.MODIFIER, new TypeFilter<>(CtClass.class));

        pb.parameter("modifier-method")
          .byRole(CtRole.MODIFIER, new TypeFilter<>(CtMethod.class));

        pb.parameter("modifier-field")
          .byRole(CtRole.MODIFIER, new TypeFilter<>(CtMethod.class));
      })
      .build();
  }
}