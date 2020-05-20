package ast.extractor.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;

public class ClassElement extends Element<CtClass<?>> {

  public ClassElement(CtClass<?> clazz) {
    super(clazz);
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
      var items = method
        .getModifiers()
        .stream()
        .map(a -> a.name())
        .collect(Collectors.toList());

      items.add(method.getSignature());
      result.add(String.join(" ", items));
    }

    return result;
  }
}