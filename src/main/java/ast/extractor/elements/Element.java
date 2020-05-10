package ast.extractor.elements;

import java.util.List;

import com.google.gson.JsonObject;

import core.Jsonify;
import ast.snapshot.Snapshot;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;

public abstract class Element<T extends CtElement> implements Jsonify<Element<?>> {

  protected T element;
  private String path;

  private Snapshot snapshot;

  Element(T el) {
    this.element = el;
    this.path = el.getPath().toString();
  }

  public Element(Snapshot snap) {
    this.snapshot = snap;
  }

  /**
   * It returns the ast element of AST
   */
  public T get() {
    return this.element;
  }

  /**
   * It returns the path of element in original source code
   */
  public String path() {
    return path;
  }

  public abstract List<Element<?>> dependencies();

  @Override
  public JsonObject toJson() {
    var obj = new JsonObject();
    obj.addProperty("element", element.toString());
    obj.addProperty("path", this.path);
    obj.addProperty("type", element.getClass().getSimpleName());
    return obj;
  }

  @Override
  public Element<?> fromJson(JsonObject content) {
    var path = content.get("path").getAsString();
    return ElementFactory.create(this.snapshot, path);
  }

  public Boolean isDirectParentOf(Element<?> other) {
    try {
      return other.get().getParent() == this.get();
    } catch (NullPointerException ex) {
      return false;
    }
  }

  public Boolean isDirectChildOf(Element<?> other) {
    try {
      return this.get().getParent() == other.get();
    } catch (NullPointerException ex) {
      return false;
    }
  }

  public Boolean isParentOf(Element<?> other) {
    try {
      return other.get().hasParent(this.get());
    } catch (NullPointerException ex) {
      return false;
    }
  }

  public Boolean isChildOf(Element<?> other) {
    try {
      return this.get().hasParent(other.get());
    } catch (NullPointerException ex) {
      return false;
    }
  }

  public CtElement getParent() {
    return element.getParent();
  }

  public List<CtVariableReference<?>> getVariables() {
    return element.getElements(new TypeFilter<>(CtVariableReference.class));
  }

  public List<CtLiteral<?>> getLiterals() {
    return element.getElements(new TypeFilter<>(CtLiteral.class));
  }
}