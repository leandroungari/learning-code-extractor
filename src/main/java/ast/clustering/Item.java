package ast.clustering;

import com.google.gson.JsonObject;

import ast.extractor.mapping.Mapping;
import ast.snapshot.Snapshot;
import persistence.Jsonify;

public class Item implements Jsonify<Item> {

  private String commit;
  private Mapping mapping;

  private Snapshot snapshot;

  public Item() {

  }

  public void setSnapshot(Snapshot s) {
    this.snapshot = s;
  }

  public Item(String commit, Mapping mapping) {
    this.commit = commit;
    this.mapping = mapping;
  }

  public String getCommit() {
    return commit;
  }

  public Mapping getMapping() {
    return mapping;
  }

  @Override
  public String toString() {
    return toJson().toString();
  }

  @Override
  public JsonObject toJson() {
    var result = new JsonObject();
    result.addProperty("commit", commit);
    result.add("mapping", mapping.toJson());
    return result;
  }

  @Override
  public Item fromJson(JsonObject content) {
    this.commit = content.get("commit").getAsString();
    this.mapping = Mapping.fromJson(
      content.get("mapping").getAsJsonObject(),
      snapshot.commit(this.commit)
    );
      
    return this;
  }
}

