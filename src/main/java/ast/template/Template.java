package ast.template;

import java.util.HashSet;

import ast.extractor.elements.Element;
import ast.extractor.mapping.Mapping;
import spoon.pattern.Pattern;
import spoon.reflect.declaration.CtElement;

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
    var matches = new HashSet<CtElement>();
    var parent = element.getParentOfAllDependencies();
    System.out.println("parent:");
    System.out.println(parent);
    pattern
      .getMatches(parent)
      .stream()
      .forEach(item -> {
        try {
          matches.add(item.getMatchingElement());
        } catch(Exception ex) {
          for(var options: item.getMatchingElements()) {
            matches.add(options);
          }
        }
      });
  
    for(var dependency: element.dependencies()) {
      if(!matches.contains(dependency)) {
        return false;
      }
    }
    return true;
  }
}