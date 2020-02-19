package ast.template.tree;

public class TreeBuilder<T> {

  private Tree<T> root;

  public TreeBuilder() {
    
  }

  public TreeBuilder<T> create(T value) {

    this.root = new Tree<T>(value);
    return this;
  }

  public TreeBuilder<T> select(String key) {
    
    var builder = new TreeBuilder<T>();
    return builder.create(root.get(key));
  }

  public void add(String key, T value) {

    root.add(key, value);
  }

  public Tree<T> result() {

    return root;
  }
}