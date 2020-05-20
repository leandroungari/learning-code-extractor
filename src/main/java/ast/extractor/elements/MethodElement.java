package ast.extractor.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

public class MethodElement extends Element<CtMethod<?>> {

  MethodElement(CtMethod<?> el) {
    super(el);
    this.priority = 2;
  }

  @Override
  public List<? extends CtElement> dependencies() {
    return List.of(element);
  }

  @Override
  public List<String> extractTokens() {
    var result = new ArrayList<String>();
    result.add(element.getSimpleName());
    result.add(String.join(
      " ", 
      element.getModifiers()
        .stream()
        .map(a -> a.name())
        .collect(Collectors.toList())
    ));

    var params = element.getParameters()
      .stream()
      .map(item -> item.getType().getSimpleName() + " " + item.getSimpleName())
      .collect(Collectors.toList());

    result.addAll(params);

    var statements = element.getBody()
      .getStatements()
      .stream()
      .map(state -> state.toString())
      .collect(Collectors.toList());

    result.addAll(statements);

    return result;
  }

  /*@Override
  protected List<CtElement> getCompleteChange() {
    return List.of(this.element);
  }*/

  /*public List<CtParameter<?>> getParamsFromMethod() {
    
    return get().getParameters();
  }

  public List<CtStatement> getMethodBlock() {

    return get().getBody().getStatements();
  }*/
}