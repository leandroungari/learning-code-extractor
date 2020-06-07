package ast.filter;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

public class LocalVariableFilter extends Filter<CtLocalVariable<?>>{
  private List<? extends CtElement> items;

  public LocalVariableFilter(List<? extends CtElement> items) {
    this.items = items;
  }

  @Override
  public List<CtLocalVariable<?>> filter() {
    var result = new HashSet<CtLocalVariable<?>>();
    for (var item: this.items) {
      result.addAll(item.getElements(new TypeFilter<>(CtLocalVariable.class)));
    }
    return result.stream().collect(Collectors.toList());
  }
}