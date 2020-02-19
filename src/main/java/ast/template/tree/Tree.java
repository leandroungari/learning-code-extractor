package ast.template.tree;

import java.util.TreeMap;

public class Tree<T> extends TreeMap<String, Tree<T>> {

  private static final long serialVersionUID = 1L;

  private T value;

  Tree(T value) {
    super();
    this.value = value;
  }

  public void add(String key, T value) {

    this.put(key, new Tree<T>(value));
  }

  public T get(String key) {

    return this.get(key);
  }
}