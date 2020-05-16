package ast.extractor.mapping;

import com.google.gson.JsonObject;

import ast.extractor.elements.ElementFactory;
import spoon.reflect.declaration.CtElement;

public class DeleteMapping extends Mapping {

  public DeleteMapping() {
    super(); 
  }

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
    json.add("source", source.toJson());
    json.addProperty("type", "delete");
    json.addProperty("oldFile", oldFile);
    return json;
  }

  @Override
  public Mapping fromJson(JsonObject content) {
    var path = DeleteMapping.pathOfElement(content, "source");
    this.source = ElementFactory.create(snapshot, path);
    this.oldFile = content.get("oldFile").getAsString();
    return this;
  }

  @Override
  public Boolean isParentOf(Mapping other) {
    return this.source().isParentOf(other.source());
  }

  @Override
  public Boolean isChildOf(Mapping other) {
    return this.source().isChildOf(other.source());
  }

  @Override
  protected Integer getPriority() {
    return source().getPriority();
  }
}