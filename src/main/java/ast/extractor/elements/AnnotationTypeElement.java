package ast.extractor.elements;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtElement;

public class AnnotationTypeElement extends Element<CtAnnotationType<?>> {

  AnnotationTypeElement(CtAnnotationType<?> el) {
    super(el);
    this.priority = 4;
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