package ast.slicing;

import java.util.List;
import java.util.stream.Collectors;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;

public class Slicer {


  private CtClass<?> element;

  public Slicer(String content) {

    element = Launcher.parseClass(content);
  }

  /**
   * [
   *  Verificar o acesso ao atributo de superclasses
   *  mesmo não tendo a declaração da superclasse
   * ]
   * @return
   */
  public List<String> getAllFields() {

    return element
      .getAllFields()
      .stream().map(field -> {
        return field.getSimpleName();
      })
      .collect(Collectors.toList());
  }

  public void getStatementsThatContainsFields() {

    element.getAllMethods().stream().map(method -> {
      method.getBody().getStatements().
    });

  }



}