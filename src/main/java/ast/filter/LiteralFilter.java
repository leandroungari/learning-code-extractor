package ast.filter;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

public class LiteralFilter extends Filter<CtLiteral<?>>{
  private List<? extends CtElement> items;

  public LiteralFilter(List<? extends CtElement> items) {
    this.items = items;
  }

  @Override
  public List<CtLiteral<?>> filter() {
    var result = new HashSet<CtLiteral<?>>();
    for (var item: this.items) {
      result.addAll(item.getElements(new TypeFilter<>(CtLiteral.class)));
    }
    return result.stream().collect(Collectors.toList());
  }
}