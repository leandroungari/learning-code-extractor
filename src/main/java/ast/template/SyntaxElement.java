package ast.template;

import java.util.LinkedHashSet;

import com.google.gson.Gson;

public class SyntaxElement {

  private String type;
  private String value;

  private LinkedHashSet<SyntaxElement> children;

  public SyntaxElement(String type, String value) {
    this.type = type;
    this.value = value;
    this.children = new LinkedHashSet<>();
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
  public LinkedHashSet<SyntaxElement> getChildren() {
    return children;
  }

  /**
   * @param children the children to set
   */
  public void setChildren(LinkedHashSet<SyntaxElement> children) {
    this.children = children;
  }

  @Override
  public String toString() {

    var gson = new Gson();
    return gson.toJson(this);
  }

}


