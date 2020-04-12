package ast.change;

import java.util.List;

import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public abstract class Change<T extends CtElement> {

  protected T element;

  public Change(T e) {
    this.element = e;
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

  @Override
  public String toString() {
    return element.toString();
  }
}