package ast.extractor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.Mappable;
import ast.extractor.change.ClassChange;
import ast.extractor.change.ConstructorChange;
import ast.extractor.change.FieldChange;
import ast.extractor.change.InterfaceChange;
import ast.extractor.change.MethodChange;
import ast.extractor.change.StatementChange;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public abstract class Change<T extends CtElement> implements Mappable {

  protected T element;
  private String path;

  public Change(T e) {
    this.element = e;
    this.path = element.getPath().toString();
  }

  public String path() {

    return path;
  }

  public abstract List<CtElement> getCompleteChange();

  public static Change<?> createChange(CtElement element) {

    var temp = element;
    do {
      if (temp instanceof CtClass) {
        return new ClassChange((CtClass<?>) temp);
      } else if (temp instanceof CtInterface) {
        return new InterfaceChange((CtInterface<?>) temp);
      } else if (temp instanceof CtConstructor) {
        return new ConstructorChange((CtConstructor<?>) temp);
      } else if (temp instanceof CtMethod) {
        return new MethodChange((CtMethod<?>) temp);
      } else if (temp instanceof CtField) {
        return new FieldChange((CtField<?>) temp);
      } else if (temp instanceof CtStatement) {
        return new StatementChange((CtStatement) temp);
      }

      temp = temp.getParent();

    } while (temp != null);

    System.err.println("aqui - " + element);
    throw new Error("Unexpected type -- " + element.getClass());

  }

  public CtClass<?> getParentClass() {

    return element.getParent(new TypeFilter<>(CtClass.class));
  }

  public CtMethod<?> getParentMethod() {

    return element.getParent(new TypeFilter<>(CtMethod.class));
  }

  public T get() {

    return element;
  }

  protected static <A, B> List<B> childrenFilter(CtElement target, Class<A> clazz, Function<A, B> action) {

    return target.getElements(new TypeFilter<>(clazz)).stream().map(item -> action.apply(item))
        .collect(Collectors.toList());
  }

  @Override
  public JsonElement toMap() {
    var json = new JsonObject();
    json.addProperty("element", element.toString());
    json.addProperty("path", path);
    json.addProperty("type", getClass().getSimpleName());
    return json;
  }

  @Override
  public String toString() {
    return toMap().toString();
  }
}