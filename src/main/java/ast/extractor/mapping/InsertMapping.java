package ast.extractor.mapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ast.extractor.elements.ElementFactory;
import spoon.reflect.declaration.CtElement;

public class InsertMapping extends Mapping {

  public InsertMapping(CtElement destination) {
    super();

    this.destination = ElementFactory.create(destination);
  }

  @Override
  public String toString() {

    return toJson().toString();
  }

  @Override
  public JsonObject toJson() {
    var json = new JsonObject();
    var listDestination = new JsonArray();
    for (var item : destination.dependencies()) {
      listDestination.add(item.toJson());
    }
    json.add("destination", listDestination);
    json.addProperty("type", "insert");
    return json;
  }

  @Override
  public Mapping fromJson(JsonObject content) {
    var path = InsertMapping.pathOfElement(content, "destination");
    this.destination = ElementFactory.create(snapshot, path);
    return this;
  }

  @Override
  public Boolean isParentOf(Mapping other) {
    return this.destination().isParentOf(other.destination());
  }

  @Override
  public Boolean isChildOf(Mapping other) {
    return this.destination().isChildOf(other.destination());
  }
}
