package ast.extractor.elements;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtEnum;

public class EnumElement extends Element<CtEnum<?>> {

  public EnumElement(CtEnum<?> el) {
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
    return result;
  }

}