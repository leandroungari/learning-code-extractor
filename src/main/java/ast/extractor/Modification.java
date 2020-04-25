package ast.extractor;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.Mappable;

public class Modification implements Mappable {

  private String oldPath;
  private String newPath;
  private List<Mapping> mappings;

  public Modification() {
    mappings = new ArrayList<>();
  }

  public Modification(Mapping mapping) {
    mappings = new ArrayList<>();
    mappings.add(mapping);
  }

  public Modification(List<Mapping> mappings) {
    this.mappings = mappings;
  }

  public List<Mapping> getMappings() {
    return mappings;
  }

  public void setOldPath(String path) {
    this.oldPath = path;
  }

  public void setNewPath(String path) {
    this.newPath = path;
  }

  public String getOldPath() {
    return oldPath;
  }

  public String getNewPath() {
    return newPath;
  }

  @Override
  public String toString() {

    return toMap().toString();
  }

  @Override
  public JsonElement toMap() {
    var json = new JsonObject();
    json.addProperty("oldPath", oldPath);
    json.addProperty("newPath", newPath);

    var array = new JsonArray();
    for (var mapping : mappings) {
      array.add(mapping.toMap());
    }
    json.add("mappings", array);

    return json;
  }

  public static Modification create(JsonObject json) {
    
    var modification = new Modification();
    modification.setNewPath(json.get("oldPath").getAsString());
    modification.setOldPath(json.get("newPath").getAsString());
    
    for(var item: json.get("mappings").getAsJsonArray()) {
      modification
        .getMappings()
        .add(Mapping.create(item.getAsJsonObject()));
    }

    return modification;
  }

}