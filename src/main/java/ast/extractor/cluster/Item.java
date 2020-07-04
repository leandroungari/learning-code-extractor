package ast.extractor.cluster;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import ast.extractor.mapping.DeleteMapping;
import ast.extractor.mapping.InsertMapping;
import ast.extractor.mapping.Mapping;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;
import ast.snapshot.Snapshot;
import persistence.Jsonify;

public class Item implements Jsonify<Item> {

  private Integer code;
  private List<String> commits;
  private Integer size;

  private List<Mapping> items;
  private Snapshot snapshot;

  public Item() {

  }

  public void setSnapshot(Snapshot snapshot) {
    this.snapshot = snapshot;
  }

  @Override
  public JsonObject toJson() {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Mapping> getMappings() {
    return items;
  }

  public Integer getSize() {
    return size;
  }

  public Integer code() {
    return code;
  }

  @Override
  public Item fromJson(JsonObject content) {
    this.code = content.get("code").getAsInt();
    this.size = content.get("size").getAsInt();

    this.commits = new ArrayList<>();
    for(var a: content.get("commits").getAsJsonArray()) {
      this.commits.add(a.getAsString());
    }

    this.items = new ArrayList<>();
    var counter = 0;
    for (var item : content.get("mappings").getAsJsonArray()) {
      var currentCommit = this.commits.get(counter);
      counter++;
      var mapping = item.getAsJsonObject();
      var type = mapping.get("type").getAsString();
      if ("insert".equals(type)) {
        var insert = new InsertMapping();
        insert.setSnapshot(snapshot.commit(currentCommit));
        this.items.add(insert.fromJson(mapping));
      } else if ("delete".equals(type)) {
        var delete = new DeleteMapping();
        delete.setSnapshot(snapshot.commit(currentCommit));
        this.items.add(delete.fromJson(mapping));
      } else if ("move".equals(type)) {
        var move = new MoveMapping();
        move.setSnapshot(snapshot.commit(currentCommit));
        this.items.add(move.fromJson(mapping));
      } else {
        var update = new UpdateMapping();
        update.setSnapshot(snapshot.commit(currentCommit));
        this.items.add(update.fromJson(mapping));
      }
    }
    return this;
  }
}