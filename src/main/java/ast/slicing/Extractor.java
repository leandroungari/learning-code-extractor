package ast.slicing;

import java.util.ArrayList;
import java.util.List;

import ast.slicing.change.Change;
import ast.slicing.change.FieldChange;
import ast.slicing.change.MethodChange;
import ast.slicing.change.StatementChange;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.path.CtPathStringBuilder;
import spoon.reflect.reference.CtFieldReference;

public class Extractor {

  private CtModel model;

  public Extractor(CtModel model) {
    this.model = model;
  }

  public void execute(String path) {

    var item = getModification(path);
    var change = verifyTypeOfModification(item);

    
  }

  public CtElement getModification(String path) {

    var mainLine = getElementByPath(path);
    return mainLine.get(0);
  }

  private List<CtElement> getElementByPath(String path) {

    var builder = new CtPathStringBuilder()
      .fromString(path);    
    return builder.evaluateOn(model.getRootPackage());
  }

  private Change<?> verifyTypeOfModification(CtElement element) {

    Change<?> change = null;

    if(element instanceof CtMethod) {
      change = new MethodChange((CtMethod<?>) element);
    }
    else if(element instanceof CtFieldReference) {
      change = new FieldChange((CtFieldReference<?>) element);
    }
    else {
      //os demais comandos
      //por enquanto os statements
      change = new StatementChange((CtStatement) element);
    }

    return change;
  }


}