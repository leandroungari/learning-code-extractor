package ast.extractor;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.Mappable;
import ast.extractor.mapping.Mapping;

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

  public List<Mapping> getMappings() {
    return mappings;
  }

  public void setOldPath(String path) {
    this.oldPath = path;
  }

  public void setNewPath(String path) {
    this.newPath = path;
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
    for(var mapping: mappings) {
      array.add(mapping.toMap());
    }
    json.add("mappings", array);
    
    return json;
  }

  
}