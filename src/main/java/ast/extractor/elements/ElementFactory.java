package ast.extractor.elements;

import ast.snapshot.CommitSnapshot;
import ast.snapshot.exception.NotFoundElementInSnapshotException;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtEnum;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;

public class ElementFactory<T extends Element<? extends CtElement>> {

  private static Boolean isLoadElementEnabled = true;
  private static String tempRepository;

  public static <A extends CtElement> Element<?> create(A item) {

    if(item == null) {
      return new UndefinedElement();
    }

    CtElement temp = item;
    do {
      if (temp instanceof CtClass) {
        return new ClassElement((CtClass<?>) temp);
      } else if (temp instanceof CtInterface) {
        return new InterfaceElement((CtInterface<?>) temp);
      } else if (temp instanceof CtEnum) {
        return new EnumElement((CtEnum<?>) temp);
      } else if (temp instanceof CtConstructor) {
        return new ConstructorElement((CtConstructor<?>) temp);
      } else if (temp instanceof CtMethod) {
        return new MethodElement((CtMethod<?>) temp);
      } else if (temp instanceof CtField) {
        return new FieldElement((CtField<?>) temp);
      } else if (temp instanceof CtStatement) {
        return new StatementElement((CtStatement) temp);
      } else if (temp instanceof CtAnnotationType) {
        return new AnnotationTypeElement((CtAnnotationType<?>) temp);
      }

      temp = temp.getParent();

    } while (temp != null);

    throw new Error("Unexpected type -- " + item.getClass());
  }

  public static <A extends CtElement> Element<?> create(
    CommitSnapshot snap, 
    String elementPath) {

    var item = snap.model(elementPath);
    var counter = 0;
    while(item == null) {
      if(counter > 5) break;
      if(snap.commit().before() == null) break;
      counter++;
      try {
        snap = snap.previous();
        item = snap.model(elementPath);
      } catch(NotFoundElementInSnapshotException ex) {
        item = null;
      }
    }
    
    return ElementFactory.create(item);
  }

  public static Element<?> create(String path, String commit) {
    return new TempElement(path, commit);
  }

  public static Boolean isLoadElementEnabled() {
    return isLoadElementEnabled;
  }

  public static void setTempRepository(String name) {
    tempRepository = name;
  }

  public static String getTempRepository() {
    return tempRepository;
  }

  public static void enableLoadElement() {
    isLoadElementEnabled = true;
  }

  public static void disableLoadElement() {
    isLoadElementEnabled = false;
  }
  
}