package ast.extractor.change;

import java.util.List;

import ast.extractor.Change;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;

public class ConstructorChange extends Change<CtConstructor<?>> {

  public ConstructorChange(CtConstructor<?> constructor) {
    super(constructor);
  }

  @Override
  protected List<CtElement> getCompleteChange() {

    return List.of(element);
  }
}