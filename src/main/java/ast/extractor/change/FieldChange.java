package ast.extractor.change;

import java.util.List;

import ast.extractor.Change;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtFieldReference;

public class FieldChange extends Change<CtFieldReference<?>> {

  public FieldChange(CtFieldReference<?> e) {
    super(e);
  }

  @Override
  public List<CtElement> getCompleteChange() {
    // TODO Auto-generated method stub
    return null;
  }
}