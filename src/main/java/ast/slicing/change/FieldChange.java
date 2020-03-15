package ast.slicing.change;

import java.util.ArrayList;
import java.util.List;

import ast.slicing.Slicer;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtFieldReference;

public class FieldChange extends Change<CtFieldReference<?>> {

  public FieldChange(CtFieldReference<?> e) {
    super(e);
  }

  @Override
  public List<CtElement> build(CtModel model) {
    var result = new ArrayList<CtElement>();

    var clazz = getParentClass();
    var slicer = new Slicer(clazz);
    //var data = slicer.getVariables(element);
    
    //System.out.println(data);

    return null;
  }
}