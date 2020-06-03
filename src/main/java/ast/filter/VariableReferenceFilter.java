package ast.filter;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class VariableReferenceFilter extends Filter<CtVariableReference<?>>{
  private List<? extends CtElement> items;

  public VariableReferenceFilter(List<? extends CtElement> items) {
    this.items = items;
  }

  @Override
  public List<CtVariableReference<?>> filter() {
    var result = new HashSet<CtVariableReference<?>>();
    for (var item: this.items) {
      result.addAll(item.getElements(new TypeFilter<>(CtVariableReference.class)));
    }
    return result.stream().collect(Collectors.toList());
  }
}