package ast.filter;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.visitor.filter.TypeFilter;

public class ModifierFilter extends Filter<CtModifiable>{
  private List<? extends CtElement> items;

  public ModifierFilter(List<? extends CtElement> items) {
    this.items = items;
  }

  @Override
  public List<CtModifiable> filter() {
    var result = new HashSet<CtModifiable>();
    for (var item: this.items) {
      result.addAll(item.getElements(new TypeFilter<>(CtModifiable.class)));
    }
    return result.stream().collect(Collectors.toList());
  }
}