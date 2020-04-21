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
  public List<CtElement> getCompleteChange() {
    // TODO Auto-generated method stub
    return null;
  }
}