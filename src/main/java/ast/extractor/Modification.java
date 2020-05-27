package ast.extractor;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ast.extractor.mapping.DeleteMapping;
import ast.extractor.mapping.InsertMapping;
import ast.extractor.mapping.Mapping;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;
import ast.snapshot.CommitSnapshot;
import core.Jsonify;

public class Modification implements Jsonify<Modification> {

  private String oldPath;
  private String newPath;
  private List<Mapping> mappings;

  private CommitSnapshot snapshot;

  public Modification() {
    mappings = new ArrayList<>();
  }

	public void setSnapshot(CommitSnapshot snap) {
		this.snapshot = snap;
	}

  public List<Mapping> getMappings() {
    return mappings;
  }

  public void setMappings(List<Mapping> mappings) {
    this.mappings = mappings;
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

    return toJson().toString();
  }

  @Override
  public JsonObject toJson() {
    var json = new JsonObject();
    json.addProperty("oldPath", oldPath);
    json.addProperty("newPath", newPath);

    var array = new JsonArray();
    for (var mapping : mappings) {
      array.add(mapping.toJson());
    }
    json.add("mappings", array);

    return json;
  }

  @Override
  public Modification fromJson(JsonObject content) {
    this.oldPath = content.get("oldPath").getAsString();
    this.newPath = content.get("newPath").getAsString();

    this.mappings = new ArrayList<>();
    for(var item: content.get("mappings").getAsJsonArray()) {
      var mapping = item.getAsJsonObject();
      var type = mapping.get("type").getAsString();
      if("insert".equals(type)) {
        var insert = new InsertMapping();
        insert.setSnapshot(this.snapshot);
        this.mappings.add(insert.fromJson(mapping));
      } else if("delete".equals(type)) {
        var delete = new DeleteMapping();
        delete.setSnapshot(this.snapshot);
        this.mappings.add(delete.fromJson(mapping));
      } else if("move".equals(type)) {
        var move = new MoveMapping();
        move.setSnapshot(this.snapshot);
        this.mappings.add(move.fromJson(mapping));
      } else {
        var update = new UpdateMapping();
        update.setSnapshot(this.snapshot);
        this.mappings.add(update.fromJson(mapping));
      }
    }
    return this;
  }

  /*public static Modification create(JsonObject json) {
    
    var modification = new Modification();
    modification.setNewPath(json.get("oldPath").getAsString());
    modification.setOldPath(json.get("newPath").getAsString());
    
    for(var item: json.get("mappings").getAsJsonArray()) {
      modification
        .getMappings()
        .add(Mapping.create(item.getAsJsonObject()));
    }

    return modification;
  }*/

}