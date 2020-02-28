package ast.template;

import java.util.LinkedHashSet;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

public class Template {

  private CtElement element;

  public Template(CtElement element) {

    this.element = element;
  }

  public LinkedHashSet<SyntaxElement> extract() {

    var syntax = new SyntaxElement(getType(element), null);
    var tree = new LinkedHashSet<SyntaxElement>();

    tree.add(syntax);
    return extractChildNodes(syntax.getChildren(), element);
  }

  private LinkedHashSet<SyntaxElement> extractChildNodes(
    LinkedHashSet<SyntaxElement> tree, 
    CtElement element) {
    
    

    element.descendantIterator().forEachRemaining(item -> {
      if(element == item.getParent()) {
        
        var syntax = new SyntaxElement(
          getType(item), 
          item.isImplicit() ? null : getValue(item)
        );
        tree.add(syntax);

        if(hasChildren(item)) {
          syntax.setChildren(extractChildNodes(syntax.getChildren(), item));
        }
        else {
          syntax.setChildren(null);
        }
        
        /*if(getType(item) != null) {
          syntax.setChildren(extractChildNodes(syntax.getChildren(), item));
        }*/
      }
    });

    return tree;
  }

  private boolean hasChildren(CtElement item) {
    
    return item.getElements(
      new TypeFilter<>(CtElement.class)
    ).size() != 1;
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