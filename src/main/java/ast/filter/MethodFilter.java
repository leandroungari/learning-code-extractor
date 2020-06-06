package ast.filter;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class MethodFilter extends Filter<CtMethod<?>> {
  private List<? extends CtElement> items;

  public MethodFilter(List<? extends CtElement> items) {
    this.items = items;
  }

  @Override
  public List<CtMethod<?>> filter() {
    var result = new HashSet<CtMethod<?>>();
    for (var item: this.items) {
      result.addAll(item.getElements(new TypeFilter<>(CtMethod.class)));
    }
    return result.stream().collect(Collectors.toList());
  }
}