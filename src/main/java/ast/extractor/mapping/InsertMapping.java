package ast.extractor.mapping;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
  public JsonElement toJson() {
    var json = new JsonObject();
    var listDestination = new JsonArray();
    for(var item: destination) {
      listDestination.add(item.toJson());
    }
    json.add("destination", listDestination);
    json.addProperty("type", "insert");
    return json;
  }

  @Override
  public Boolean isParentOf(Mapping other) {
    if (isSameType(other)) {
      var numberOfMatches = 0;
      var otherMapping = (InsertMapping) other;
      for (var otherChange : otherMapping.destination()) {
        for (var change : this.destination()) {
          if (otherChange.get().hasParent(change.get())) {
            numberOfMatches++;
          }
        }
      }
      return verifyCorrespondence(
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
      var otherMapping = (InsertMapping) other;
      for (var change : this.destination()) {
        for (var otherChange : otherMapping.destination()) {
          if (change.get().hasParent(otherChange.get())) {
            numberOfMatches++;
          }
        }
      }
      return verifyCorrespondence(
        this.destination().size(), 
        numberOfMatches
      );
    }
    return false;
  }
}