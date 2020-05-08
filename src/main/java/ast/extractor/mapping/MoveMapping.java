package ast.extractor.mapping;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.extractor.elements.ElementFactory;
import spoon.reflect.declaration.CtElement;

public class MoveMapping extends Mapping {

  public MoveMapping(CtElement source, CtElement destination) {

    this.source = ElementFactory.create(source);
    this.destination = ElementFactory.create(destination);
  }

  @Override
  public String toString() {

    return toJson().toString();
  }

  @Override
  public JsonElement toJson() {
    var json = new JsonObject();
    var listSource = new JsonArray();
    for(var item: source) {
      listSource.add(item.toJson());
    }
    json.add("source", listSource);

    var listDestination = new JsonArray();
    for(var item: destination) {
      listDestination.add(item.toMap());
    }
    json.add("destination", listDestination);
    json.addProperty("type", "move");

    return json;
  }

  @Override
  public Boolean isParentOf(Mapping other) {
    if (isSameType(other)) {
      var numberOfMatches = 0;

      var otherMapping = (MoveMapping) other;
      for (var otherChange : otherMapping.source()) {
        for (var change : this.source()) {
          if (otherChange.get().hasParent(change.get())) {
            numberOfMatches++;
          }
        }
      }
      for (var otherChange : otherMapping.destination()) {
        for (var change : this.destination()) {
          if (otherChange.get().hasParent(change.get())) {
            numberOfMatches++;
          }
        }
      }

      return verifyCorrespondence(
        otherMapping.source().size() + 
        otherMapping.destination().size(),
        numberOfMatches
      );
    }
    return false;
  }

  @Override
  public Boolean isChildOf(Mapping other) {
    if (isSameType(other)) {
      var numberOfMatches = 0;

      var otherMapping = (MoveMapping) other;
      for (var change : this.source()) {
        for (var otherChange : otherMapping.source()) {
          if (change.get().hasParent(otherChange.get())) {
            numberOfMatches++;
          }
        }
      }
      for (var change : this.destination()) {
        for (var otherChange : otherMapping.destination()) {
          if (change.get().hasParent(otherChange.get())) {
            numberOfMatches++;
          }
        }
      }

      return verifyCorrespondence(
        otherMapping.source().size() + 
        otherMapping.destination().size(),
        numberOfMatches
      );
    }
    return false;
  }
}