package ast.extractor.change;

import java.util.List;

import ast.extractor.Change;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;

public class FieldChange extends Change<CtField<?>> {

  public FieldChange(CtField<?> e) {
    super(e);
  }

  @Override
  protected List<CtElement> getCompleteChange() {
    return List.of(this.element);
  }
}