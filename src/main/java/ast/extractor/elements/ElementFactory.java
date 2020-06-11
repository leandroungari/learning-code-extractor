package ast.extractor.elements;

import ast.snapshot.CommitSnapshot;
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
    String elementPath, 
    String filename) {

    var item = snap.model(elementPath);
    while(item == null) {
      if(snap.commit().before() == null) break;
      snap = snap.previous();
      item = snap.model(elementPath);
    }
    
    return ElementFactory.create(item);
  }

  /* 
  public CtClass<?> getParentClass() {

    return element.getParent(new TypeFilter<>(CtClass.class));
  }

  public T get() {

    return element;
  }

  protected static <A, B> List<B> childrenFilter(CtElement target, Class<A> clazz, Function<A, B> action) {

    return target.getElements(new TypeFilter<>(clazz)).stream().map(item -> action.apply(item))
        .collect(Collectors.toList());
  }

  public List<CtVariableReference<?>> getVariablesDependencies() {

    return Change.childrenFilter(
      element, 
      CtVariableReference.class, 
      item -> item
    );
  }

  public List<CtLiteral<?>> getLiterals() {
    return Change.childrenFilter(
      element, 
      CtLiteral.class, 
      item -> item
    );
  }

  @Override
  public JsonElement toMap() {
    var json = new JsonObject();
    json.addProperty("element", element.toString());
    json.addProperty("path", path);
    json.addProperty("type", getClass().getSimpleName());
    return json;
  }

  @Override
  public String toString() {
    return toMap().toString();
  }
  */
}