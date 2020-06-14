package ast.extractor.mapping;

import com.google.gson.JsonObject;

import ast.extractor.elements.ElementFactory;
import spoon.reflect.declaration.CtElement;

public class UpdateMapping extends Mapping {

  public UpdateMapping() {
    super(); 
  }

  public UpdateMapping(CtElement source, CtElement destination) {

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
    json.add("source", source.toJson());
    json.add("destination", destination.toJson());
    json.addProperty("type", "update");
    json.addProperty("oldFile", oldFile);
    json.addProperty("newFile", newFile);

    json.addProperty("oldCommit", oldCommit);
    json.addProperty("newCommit", newCommit);

    return json;
  }

  @Override
  public Mapping fromJson(JsonObject content) {
    this.oldFile = content.get("oldFile").getAsString();
    this.newFile = content.get("newFile").getAsString();

    this.newCommit = content.get("newCommit").getAsString();
    var destinationPath = UpdateMapping
      .pathOfElement(content, "destination");
    this.destination = ElementFactory
      .create(snapshot, destinationPath, this.newFile);

    this.oldCommit = content.get("oldCommit").getAsString();
    var sourcePath = UpdateMapping
      .pathOfElement(content, "source");

    var oldSnap = snapshot.getParent().commit(oldCommit);
    this.source = ElementFactory
      .create(oldSnap, sourcePath, this.oldFile);

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