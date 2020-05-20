package ast.extractor.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;

public class InterfaceElement extends Element<CtInterface<?>> {

  InterfaceElement(CtInterface<?> el) {
    super(el);
    this.priority = 1;
  }

  @Override
  public List<? extends CtElement> dependencies() {
    return List.of(element);
  }

  @Override
  public List<String> extractTokens() {
    var result = new ArrayList<String>();
    result.add(element.getSimpleName());
    for(var field: element.getAllFields()) {
      var items = field
        .getModifiers()
        .stream()
        .map(a -> a.name())
        .collect(Collectors.toList());

      items.add(field.getType().getSimpleName());
      items.add(field.getSimpleName());

      result.add(String.join(" ", items));
    }

    for(var method: element.getAllMethods()) {
      result.add(method.getSignature());
    }
    
    return result;
  }
  
}