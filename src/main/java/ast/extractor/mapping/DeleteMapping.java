package ast.extractor.mapping;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.extractor.Change;
import ast.extractor.Mapping;
import spoon.reflect.declaration.CtElement;

public class DeleteMapping extends Mapping {

  public DeleteMapping(CtElement source) {
    super();
    this.source = new ArrayList<>();

    for (var element : dependencies(source)) {
      this.source.add(Change.createChange(element));
    }
  }

  public List<Change<?>> source() {

    return source;
  }

  @Override
  public String toString() {

    return toMap().toString();
  }

  @Override
  public JsonElement toMap() {
    var json = new JsonObject();
    var listSource = new JsonArray();
    for (var item : source) {
      listSource.add(item.toMap());
    }
    json.add("source", listSource);
    return json;
  }

  @Override
  public Boolean isParentOf(Mapping other) {
    if (isSameType(other)) {
      var numberOfMatches = 0;
      var otherMapping = (DeleteMapping) other;
      for (var otherChange : otherMapping.source()) {
        for (var change : this.source()) {
          if (otherChange.get().hasParent(change.get())) {
            numberOfMatches++;
          }
        }
      }
      return verifyCorrespondence(
        otherMapping.source().size(), 
        numberOfMatches
      );
    }
    return false;
  }

  @Override
  public Boolean isChildOf(Mapping other) {
    if (isSameType(other)) {
      var otherMapping = (DeleteMapping) other;
      var numberOfMatches = 0;
      for (var change : this.source()) {
        for (var otherChange : otherMapping.source()) {
          if (change.get().hasParent(otherChange.get())) {
            numberOfMatches++;
          }
        }
      }
      return verifyCorrespondence(
        this.source().size(), 
        numberOfMatches
      );
    }
    return false;
  }
}