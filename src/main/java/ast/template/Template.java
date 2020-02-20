package ast.template;

import ast.template.syntax.SyntaxElement;
import spoon.reflect.declaration.CtElement;

public class Template {

  private CtElement element;

  public Template(CtElement element) {

    this.element = element;
  }

  public Tree<SyntaxElement> extract() {

    var syntax = new SyntaxElement(getType(element), null);
    var tree = new Tree<SyntaxElement>();

    
  }

  private Tree<SyntaxElement> extractChildNodes(
    Tree<SyntaxElement> builder, 
    CtElement element) {
    
    element.descendantIterator().forEachRemaining(item -> {
      if(element == item.getParent()) {
        
        builder.add(
          getType(item), 
          item.descendantIterator().hasNext() ? null : getValue(item)
        );
        
        if(getType(item) != null) {
          extractChildNodes(builder.select(getType(item)), item);
        }
      }
    });

    return builder;
  }

  public String getValue(CtElement element) {

    if(element == null) return null;
    return element.toString();
  }

  public String getType(CtElement element) {

    if(element == null) return null;
    return element.getClass().getSimpleName();
  }


}