package ast.extractor.mapping;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.extractor.Change;
import spoon.reflect.declaration.CtElement;

public class InsertMapping extends Mapping {

  public InsertMapping(CtElement destination) {
    super();
    this.destination = Change.createChange(destination);
  }

  public Change<?> destination() {

    return destination;
  }

  @Override
  public String toString() {

    return toMap().toString();
  }

  @Override
  public JsonElement toMap() {
    var json = new JsonObject();
    json.add("destination", destination.toMap());
    return json;
  }

  @Override
  public Boolean isParentOf(Mapping other) {
    if(isSameType(other)) {
      var element = (InsertMapping) other;
      try {
        return element.destination().get().hasParent(destination.get());
      } catch(NullPointerException ex) {
        System.err.println("== Erro na definição da modificação ==");
        System.out.println(element.destination.path());
      }
    }
    return false;
  }

  @Override
  public Boolean isChildOf(Mapping other) {
    if(isSameType(other)) {
      var element = (InsertMapping) other;
      try {
        return destination.get().hasParent(element.destination().get());
      } catch(NullPointerException ex) {
        System.err.println("== Erro na definição da modificação ==");
        System.out.println(element.destination.path());
      }
    }
    return false;
  }


}