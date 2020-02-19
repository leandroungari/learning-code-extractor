package ast.template;

import java.util.ArrayList;

import ast.template.tree.Tree;
import ast.template.tree.TreeBuilder;
import spoon.reflect.declaration.CtElement;

public class Template {

  private CtElement element;

  public Template(CtElement element) {

    this.element = element;
  }

  public Tree<String> extract() {

    var builder = new TreeBuilder<String>();
    builder.add(getType(element), getValue(element));

    var list = new ArrayList<CtElement>();
    list.add(this.element);
    
    do {

      list.get(0).descendantIterator().forEachRemaining(item -> {
        if(item.getParent() == list.get(0)) {
          builder.add(getType(element), getValue(element));
          list.add(item);
        }
      });

      list.remove(0);  
      if(list.get(0) != null) {
        builder = builder.select(getType(list.get(0)));
      }

    } while(list.get(0) != null);



    return builder.result();
  }

  public String getValue(CtElement element) {

    return element.toString();
  }

  public String getType(CtElement element) {

    return element.getClass().getSimpleName();
  }


}