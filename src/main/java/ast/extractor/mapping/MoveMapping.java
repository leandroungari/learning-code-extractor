package ast.extractor.mapping;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.extractor.Change;
import spoon.reflect.declaration.CtElement;

public class MoveMapping extends Mapping {

  public MoveMapping(CtElement source, CtElement destination) {
    this.source = Change.createChange(source);
    this.destination = Change.createChange(destination);
  }

  public Change<?> source() {
    
    return source;
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
    json.addProperty("source", source.toString());
    json.addProperty("destination", destination.toString());

    return json;
  }

  @Override
  public Boolean isParentOf(Mapping other) {
    if(isSameType(other)) {
      var element = (MoveMapping) other;
      try {
        return (
          element.source().get().hasParent(source.get()) &&
          element.destination().get().hasParent(destination.get())
        );
      } catch(NullPointerException ex) {
        System.err.println("== Erro na definição da modificação ==");
        System.out.println(element.source.path());
        System.out.println(element.destination.path());
      }
      
    }
    return false;
  }

  @Override
  public Boolean isChildOf(Mapping other) {
    if(isSameType(other)) {
      var element = (MoveMapping) other;
      try {
        return (
          source.get().hasParent(element.source().get()) &&
          destination.get().hasParent(element.destination().get())
        );
      } catch(NullPointerException ex) {
        System.err.println("== Erro na definição da modificação ==");
        System.out.println(element.source.path());
        System.out.println(element.destination.path());
      }
    }
    return false;
  }

}