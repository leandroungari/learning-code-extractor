package ast.extractor.elements;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.declaration.CtElement;

public class UndefinedElement extends Element<CtElement> {

  public UndefinedElement() {
    super(null);
    this.priority = 1000;
  }

  @Override
  public List<? extends CtElement> dependencies() {
    return List.of();
  }

  @Override
  public List<String> extractTokens() {
    return new ArrayList<String>();
  }
}