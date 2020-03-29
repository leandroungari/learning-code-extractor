package ast.slicing.change;

import java.util.List;

import ast.slicing.Slicer;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public abstract class Change<T extends CtElement> {

  protected T element;
  protected Slicer slicer;

  public Change(T e) {
    this.element = e;
    this.slicer = new Slicer(
      getParentClass()
    );
  }

  public abstract List<CtElement> build(CtModel model);

  public CtClass<?> getParentClass() {
    
    return element.getParent(new TypeFilter<>(CtClass.class));
  }

  public CtMethod<?> getParentMethod() {

    return element.getParent(new TypeFilter<>(CtMethod.class));
  }

  public T get() {
    
    return element;
  }
}