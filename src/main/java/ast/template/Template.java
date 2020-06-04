package ast.template;

import java.util.stream.Collectors;

import ast.extractor.elements.Element;
import ast.extractor.mapping.Mapping;
import spoon.pattern.Pattern;

public class Template {
  
  private Pattern input;
  private Pattern output;

  public Template(Pattern in, Pattern out) {
    this.input = in;
    this.output = out;
  }

  public Pattern getInput() {
    return input;
  }

  public Pattern getOutput() {
    return output;
  }

  public Boolean matches(Mapping mapping) {
    return this.compatibility(input, mapping.source())
      && this.compatibility(output, mapping.destination());
  }

  private Boolean compatibility(Pattern pattern, Element<?> element) {
    var matches = pattern
      .getMatches(element.getParentOfAllDependencies())
      .stream()
      .map(item -> item.getMatchingElement())
      .collect(Collectors.toList());

    System.out.println(matches);
    System.out.println("====");
    System.out.println(element.dependencies());
  
    for(var dependency: element.dependencies()) {
      if(!matches.contains(dependency)) {
        return false;
      }
    }
    return true;
  }
}