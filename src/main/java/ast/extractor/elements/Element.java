package ast.extractor.elements;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

  protected Integer priority;

  Element(T el) {
    this.element = el;
    this.path = el.getPath().toString();
  }

  public Element(Snapshot snap) {
    this.snapshot = snap;
  }

  public Integer getPriority() {
    return priority;
  }

  /**
   * It returns the ast element of AST
   */
  public T get() {
    return element;
  }

  /**
   * It returns the path of element in original source code
   */
  public String path() {
    return path;
  }

  public abstract List<? extends CtElement> dependencies();

  @Override
  public JsonObject toJson() {
    var obj = new JsonObject();
    obj.addProperty("element", this.dependencies().toString());
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
      for(var parent: this.dependencies()) {
        var isParent = true;
        for(var child: other.dependencies()) {
          if(child.getParent() != parent) {
            isParent = false;
            break;
          }
        }
        if(isParent) {
          return true;
        }
      }
    } catch (NullPointerException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  public Boolean isDirectChildOf(Element<?> other) {
    try {
      for(var child: this.dependencies()) {
        var isChild = true;
        for(var parent: other.dependencies()) {
          if(child.getParent() != parent) {
            isChild = false;
            break;
          }
        }
        if(isChild) {
          return true;
        }
      }
    } catch (NullPointerException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  public Boolean isParentOf(Element<?> other) {
    try {
      for(var parent: this.dependencies()) {
        var isParent = true;
        for(var child: other.dependencies()) {
          if(child.hasParent(parent)) {
            isParent = false;
            break;
          }
        }
        if(isParent) {
          return true;
        }
      }
    } catch (NullPointerException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  public Boolean isChildOf(Element<?> other) {
    try {
      for(var child: this.dependencies()) {
        var isChild = true;
        for(var parent: other.dependencies()) {
          if(child.hasParent(parent)) {
            isChild = false;
            break;
          }
        }
        if(isChild) {
          return true;
        }
      }
    } catch (NullPointerException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  /**
   * It retrieves the parent element based on
   * the first element from list of dependencies
   */
  public CtElement getParent() {
    return element.getParent();
  }

  public List<CtVariableReference<?>> getVariables() {
    var result = new HashSet<CtVariableReference<?>>();
    for (var item: this.dependencies()) {
      result.addAll(item.getElements(new TypeFilter<>(CtVariableReference.class)));
    }
    return result.stream().collect(Collectors.toList());
  }

  public List<CtLiteral<?>> getLiterals() {
    var result = new HashSet<CtLiteral<?>>();
    for (var element: this.dependencies()) {
      result.addAll(element.getElements(new TypeFilter<>(CtLiteral.class)));
    }
    return result.stream().collect(Collectors.toList());
  }
}