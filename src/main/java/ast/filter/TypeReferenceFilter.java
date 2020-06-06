package ast.filter;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class TypeReferenceFilter extends Filter<CtTypeReference<?>> {
  private List<? extends CtElement> items;

  public TypeReferenceFilter(List<? extends CtElement> items) {
    this.items = items;
  }

  @Override
  public List<CtTypeReference<?>> filter() {
    var result = new HashSet<CtTypeReference<?>>();
    for (var item: this.items) {
      result.addAll(item.getElements(new TypeFilter<>(CtTypeReference.class)));
    }
    return result.stream().collect(Collectors.toList());
  }
}