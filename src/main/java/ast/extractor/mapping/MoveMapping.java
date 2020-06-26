package ast.extractor.mapping;

import com.google.gson.JsonObject;

import ast.extractor.elements.ElementFactory;
import spoon.reflect.declaration.CtElement;

public class MoveMapping extends Mapping {

  public MoveMapping() {
    super(); 
  }

  public MoveMapping(CtElement source, CtElement destination) {

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
    json.add("source", source().toJson());
    json.add("destination", destination().toJson());
    json.addProperty("type", "move");
    json.addProperty("oldFile", oldFile);
    json.addProperty("newFile", newFile);

    return json;
  }

  @Override
  public Mapping fromJson(JsonObject content) {
    this.oldFile = content.get("oldFile").getAsString();
    this.newFile = content.get("newFile").getAsString();

    var destinationPath = MoveMapping
      .pathOfElement(content, "destination");
    if(snapshot != null) {
      destination = ElementFactory.create(snapshot, destinationPath);
    } else {
      destination = ElementFactory.create(destinationPath, commit);
    }
    
    var sourcePath = MoveMapping
      .pathOfElement(content, "source");
    if(snapshot != null) {
      source = ElementFactory.create(snapshot.previous(), sourcePath);
    } else {
      source = ElementFactory.create(sourcePath, commit);
    }

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

  @Override
  protected Integer getPriority() {
    var source = source().getPriority();
    var destination = destination().getPriority();
    if(source < destination) {
      return source;
    }
    return destination;
  }
}