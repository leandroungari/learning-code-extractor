package ast.extractor.mapping;

import com.google.gson.JsonObject;

import ast.extractor.elements.ElementFactory;
import spoon.reflect.declaration.CtElement;

public class InsertMapping extends Mapping {

  public InsertMapping() {
    super(); 
  }

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
    json.add("destination", destination().toJson());
    json.addProperty("type", "insert");
    json.addProperty("newFile", newFile);
    return json;
  }

  @Override
  public Mapping fromJson(JsonObject content) {
    var path = InsertMapping.pathOfElement(content, "destination");
    this.newFile = content.get("newFile").getAsString();
    if(snapshot != null) {
      destination = ElementFactory.create(snapshot, path);
    } else {
      destination = ElementFactory.create(path, commit);
    }
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

  @Override
  protected Integer getPriority() {
    return destination().getPriority();
  }
}
