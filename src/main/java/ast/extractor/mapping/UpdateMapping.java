package ast.extractor.mapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ast.extractor.elements.ElementFactory;
import spoon.reflect.declaration.CtElement;

public class UpdateMapping extends Mapping {

  public UpdateMapping(
    CtElement source, 
    CtElement destination) {

    this.source = ElementFactory.create(source);
    this.destination = ElementFactory.create(destination);
  }

  @Override
  public String toString() {

    return toJson().toString();
  }

  @Override
  public JsonObject toJson() {
    var json = new JsonObject();
    var listSource = new JsonArray();
    for(var item: source.dependencies()) {
      listSource.add(item.toJson());
    }
    json.add("source", listSource);
    
    var listDestination = new JsonArray();
    for(var item: destination.dependencies()) {
      listDestination.add(item.toJson());
    }
    json.add("destination", listDestination);
    json.addProperty("type", "update");

    return json;
  }

  @Override
  public Mapping fromJson(JsonObject content) {
    var destinationPath = MoveMapping.pathOfElement(content, "destination");
    this.destination = ElementFactory.create(snapshot, destinationPath);

    var sourcePath = MoveMapping.pathOfElement(content, "source");
    this.source = ElementFactory.create(snapshot, sourcePath);
    return this;
  }

  @Override
  public Boolean isParentOf(Mapping other) {
    return this.source().isParentOf(other.source())
      && this.destination().isParentOf(other.destination());
  }

  @Override
  public Boolean isChildOf(Mapping other) {
    return this.source().isChildOf(other.source())
      && this.destination().isChildOf(other.destination());
  }
}