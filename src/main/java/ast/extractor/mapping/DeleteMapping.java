package ast.extractor.mapping;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.extractor.Change;
import spoon.reflect.declaration.CtElement;

public class DeleteMapping extends Mapping {

  public DeleteMapping(CtElement source) {
    super();
    this.source = Change.createChange(source);
    System.out.println(this.source.getClass());
  }

  public Change<?> source() {

    return source;
  }

  @Override
  public String toString() {
    
    return toMap().toString();
  }

  @Override
  public JsonElement toMap() {
    var json = new JsonObject();
    json.addProperty("source", source.toString());

    return json;
  }

  @Override
  public Boolean isParentOf(Mapping other) {
    if(isSameType(other)) {
      var element = (DeleteMapping) other;
      try {
        return element.source().get().hasParent(source.get());
      } catch(NullPointerException ex) {
        System.err.println("== Erro na definição da modificação ==");
        System.out.println(element.source().path());
      }
    }
    return false;
  }

  @Override
  public Boolean isChildOf(Mapping other) {
    if(isSameType(other)) {
      var element = (DeleteMapping) other;
      try {
        return source.get().hasParent(element.source().get());
      } catch(NullPointerException ex) {
        System.err.println("== Erro na definição da modificação ==");
        System.out.println(element.source().path());
      }
    }
    return false;
  }
  

}