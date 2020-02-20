package ast.template;

import java.util.TreeSet;

import ast.template.syntax.Treeable;

public class Tree<T extends Treeable<T>> extends TreeSet<T> {

  private static final long serialVersionUID = 1L;

  public Tree() {
    super();
  }

  public Tree<T> insert(T value) {

    this.add(value);

    return value.getChildren();
  }

  public Tree<T> search(T item) {

    return null;
  }
}