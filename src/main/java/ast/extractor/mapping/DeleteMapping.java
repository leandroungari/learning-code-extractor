package ast.extractor.mapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.extractor.elements.ElementFactory;
import spoon.reflect.declaration.CtElement;

public class DeleteMapping extends Mapping {

  public DeleteMapping(CtElement source) {
    super();
    this.source = ElementFactory.create(source);
  }

  @Override
  public String toString() {

    return toJson().toString();
  }

  @Override
  public JsonObject toJson() {
    var json = new JsonObject();
    var listSource = new JsonArray();
    for (var item : source.dependencies()) {
      listSource.add(item.toJson());
    }
    json.add("source", listSource);
    json.addProperty("type", "delete");
    return json;
  }

  @Override
  public Mapping fromJson(JsonObject content) {
    
    var item = content.get("source").getAsJsonObject();
    this.source = 
    
    return this;
  }

  @Override
  public Boolean isParentOf(Mapping other) {
    return other.source().get().hasParent(source().get());
  }

  @Override
  public Boolean isChildOf(Mapping other) {
    return source.get().hasParent(other.source().get());
  }
}