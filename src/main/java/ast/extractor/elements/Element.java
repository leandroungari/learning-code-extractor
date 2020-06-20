package ast.extractor.elements;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import persistence.Jsonify;
import ast.extractor.Tokenizer;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;

public abstract class Element<T extends CtElement> implements Jsonify<Element<?>>, Tokenizer {

  protected T element;
  private String path;

  protected Integer priority;

  Element(T el) {
    this.element = el;
    if(el != null) {
      this.path = el.getPath().toString();
    } else {
      this.path = "#undefined";
    }
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

  public abstract List<String> extractTokens();

  @Override
  public JsonObject toJson() {
    var obj = new JsonObject();
    var list = new JsonArray();
    for(var item: this.dependencies()) {
      list.add(item.toString());
    }
    obj.add("element", list);
    obj.addProperty("path", this.path);
    obj.addProperty("type", this.getClass().getSimpleName());
    return obj;
  }

  public String type() {
    return this.getClass().getSimpleName();
  }

  @Override
  public Element<?> fromJson(JsonObject content) {
    return this;
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

  public CtElement getParentOfAllDependencies() {
    var items = this.dependencies();
    CtElement parent = null;
    try {
      parent = items.get(0);
      for(var i = 1; i < items.size(); i++) {
        var item = items.get(i);
        if(!item.hasParent(parent)) {
          parent = item.getParent();
        }
      }
    } catch(Exception error) {
      parent = this.getParent();
    }
    return parent;
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