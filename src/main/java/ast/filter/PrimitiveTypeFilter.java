package ast.filter;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javassist.CtPrimitiveType;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

public class PrimitiveTypeFilter extends Filter<CtPrimitiveType> {
  private List<? extends CtElement> items;

  public PrimitiveTypeFilter(List<? extends CtElement> items) {
    this.items = items;
  }

  @Override
  public List<CtPrimitiveType> filter() {
    var result = new HashSet<CtPrimitiveType>();
    for (var item: this.items) {
      result.addAll(item.getElements(new TypeFilter<>(CtPrimitiveType.class)));
    }
    return result.stream()
      .collect(Collectors.toList());
  }
}