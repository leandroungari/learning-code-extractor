package ast.template;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.extractor.mapping.Mapping;
import ast.filter.LiteralFilter;
import ast.filter.VariableReferenceFilter;
import spoon.pattern.ConflictResolutionMode;
import spoon.pattern.Pattern;
import spoon.pattern.PatternBuilder;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtVariableReference;

public class TemplateBuilder {

  private List<? extends CtElement> sources;
  private List<? extends CtElement> destinations;

  private List<CtVariableReference<?>> variables;
  private List<CtLiteral<?>> literals;

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

        for(var reference: this.variables) {  
          pb.parameter(reference.getSimpleName())
            .byString(reference.getSimpleName());
        }
        for(var literal: this.literals) {
          pb.parameter(literal.toString())
            .byString(literal.toString());
        }

        //filtrar o nome de métodos, atributos e classes
        //para cada variavel, verificar se tem declaração, se sim, verificar a declaração também
      })
      .build();
  }
}