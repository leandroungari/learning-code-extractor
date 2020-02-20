package ast.template.syntax;

import ast.template.Tree;

public class SyntaxElement implements Treeable<SyntaxElement> {

  private String type;
  private String value;

  private Tree<SyntaxElement> children;

  public SyntaxElement(String type, String value) {
    this.type = type;
    this.value = value;
    this.children = new Tree<SyntaxElement>();
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @return the children
   */
  public Tree<SyntaxElement> getChildren() {
    return children;
  }

  /**
   * @param children the children to set
   */
  public void setChildren(Tree<SyntaxElement> children) {
    this.children = children;
  }
}


