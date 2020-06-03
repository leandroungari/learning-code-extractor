package ast.filter;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.visitor.filter.TypeFilter;

public class FieldFilter extends Filter<CtField<?>>{
  private List<? extends CtElement> items;

  public FieldFilter(List<? extends CtElement> items) {
    this.items = items;
  }

  @Override
  public List<CtField<?>> filter() {
    var result = new HashSet<CtField<?>>();
    for (var item: this.items) {
      result.addAll(item.getElements(new TypeFilter<>(CtField.class)));
    }
    return result.stream().collect(Collectors.toList());
  }
}