package ast.extractor.change;

import java.util.List;

import ast.extractor.Change;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtInterface;

public class InterfaceChange extends Change<CtInterface<?>> {

  public InterfaceChange(CtInterface<?> item) {
    super(item);
  }

  @Override
  public List<CtElement> getCompleteChange() {
    return List.of(element);
  }

}
