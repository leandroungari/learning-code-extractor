package ast.extractor;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import core.Jsonify;

public class CommitModifications implements Jsonify<CommitModifications> {

  private String commit;
  private List<Modification> modifications;

  public CommitModifications(String commitId) {
    this.commit = commitId;
    this.modifications = new ArrayList<>();
  }

  public void add(Modification modification) {
    modifications.add(modification);
  }

  public List<Modification> getModifications() {
    return modifications;
  }

  @Override
  public String toString() {
    return toJson().toString();
  }

  @Override
  public JsonObject toJson() {
    var json = new JsonObject();
    json.addProperty("id", commit);
    var list = new JsonArray();
    for(var change: this.modifications) {
      list.add(change.toJson());
    }
    json.add("result", list);
    return json;
  }

  @Override
  public CommitModifications fromJson(JsonObject content) {
    this.commit = content.get("id").getAsString();
    this.modifications = new ArrayList<>();
    for(var item: content.get("result").getAsJsonArray()) {
      var modification = new Modification();
      modifications.add(modification.fromJson(item.getAsJsonObject()));
    }
    return this;
  }

}