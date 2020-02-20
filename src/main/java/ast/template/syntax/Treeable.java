package ast.template.syntax;

import ast.template.Tree;

public interface Treeable<T extends Treeable<T>> {

  Tree<T> getChildren();
  void setChildren(Tree<T> children);
}