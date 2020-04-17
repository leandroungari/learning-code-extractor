package ast.extractor;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.Mappable;
import ast.extractor.change.ClassChange;
import ast.extractor.change.FieldChange;
import ast.extractor.change.MethodChange;
import ast.extractor.change.StatementChange;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtFieldReference;
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
    System.out.println(element.getPath());
    if (element instanceof CtClass) {
      return new ClassChange((CtClass<?>) element);
    } else if (element instanceof CtMethod) {
      return new MethodChange((CtMethod<?>) element);
    } else if (element instanceof CtFieldReference) {
      return new FieldChange((CtFieldReference<?>) element);
    } else if (element instanceof CtStatement) {
      return new StatementChange((CtStatement) element);
    } else {
      CtElement parent = element.getParent();
      do {
        if(parent instanceof CtClass){
          return new ClassChange((CtClass<?>) parent);
        } else if(parent instanceof CtMethod) {
          return new MethodChange((CtMethod<?>) parent);
        } else if (parent instanceof CtField) {
          return new FieldChange((CtFieldReference<?>) parent);
        } else if(parent instanceof CtStatement) {
          return new StatementChange((CtStatement) parent);
        }
        parent = parent.getParent();
      } while (parent != null);

      throw new Error("Unexpected type -- " + element.getClass());
    }
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