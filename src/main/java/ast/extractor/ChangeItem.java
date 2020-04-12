package ast.extractor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.Mappable;
import ast.OperationType;
import ast.change.Change;

public class ChangeItem implements Mappable {

  private Change<?> source;
  private Change<?> destination;
  private OperationType type;

  ChangeItem() {

  }

  public ChangeItem source(Change<?> src) {
    source = src;
    return this;
  }

  public ChangeItem destination(Change<?> dst) {
    destination = dst;
    return this;
  }

  public ChangeItem type(OperationType type) {
    this.type = type;
    return this;
  }

  public OperationType type() {
    return type;
  }

  public Change<?> source() {
    return source;
  }

  public Change<?> destination() {
    return destination;
  }

  public JsonElement toMap() {
    var json = new JsonObject();
    json.addProperty("source", source != null ? source.toString() : "null");
    json.addProperty("destination", destination != null ? destination.toString() : "null");
    json.addProperty("type", type.toString());
    return json;
  }
}