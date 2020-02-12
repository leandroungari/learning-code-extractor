package ast.slicing;

import java.util.List;
import java.util.stream.Collectors;

import spoon.Launcher;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.visitor.filter.FieldAccessFilter;

public class Slicer {

  private CtClass<?> element;

  public Slicer(String content) {

    element = Launcher.parseClass(content);
  }

  /**
   * [ Verificar o acesso ao atributo de superclasses mesmo não tendo a declaração
   * da superclasse ]
   * 
   * @return
   */
  public List<CtFieldReference<?>> getAllFields() {

    return element.getAllFields().stream().collect(Collectors.toList());
  }

  public void getStatementsThatContainsFields() {

    

  }

  private List<CtFieldAccess<?>> statementContainsFieldCall(
    CtStatement state, 
    CtFieldReference<?> ref) {

    return state.getElements(new FieldAccessFilter(ref));
  }



}