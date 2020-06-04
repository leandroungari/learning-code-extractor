package ast.clustering;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.extractor.mapping.Mapping;
import ast.snapshot.Snapshot;
import core.Jsonify;

public class ClusterItem implements Jsonify<ClusterItem> {

  private Integer code;
  private Item base;
  private List<Item> items;

  private Cluster parent;

  ClusterItem(Mapping mapping, String commit, Integer code, Cluster parent) {
    this.base = new Item(commit, mapping);
    this.items = new ArrayList<>();
    this.code = code;
    this.parent = parent;
    
    this.add(mapping, commit);
  }

  ClusterItem(Cluster parent) {
    this.parent = parent;
  }

  public void add(Mapping mapping, String commit) {
    items.add(new Item(commit, mapping));
  }

  public Integer getCode() {
    return code;
  }

  public List<Item> getItems() {
    return items;
  }

  public Item getBase() {
    return base;
  }

  @Override
  public String toString() {
    return toJson().toString();
  }


  @Override
  public JsonObject toJson() {
    var result = new JsonObject();
    result.add("pattern", base.toJson());
    var list = new JsonArray();
    for(var item: items) {
      list.add(item.toJson());
    }
    result.add("items", list);
    return result;
  }

  @Override
  public ClusterItem fromJson(JsonObject content) {
    try {
      var snapshot = new Snapshot(parent.repository(), parent.branch());
      this.base = new Item().fromJson(content.get("base").getAsJsonObject());
      this.items = new ArrayList<>();
      for(var item: content.get("items").getAsJsonArray()) {
        var instance = new Item();
        instance.setSnapshot(snapshot);
        this.items.add(instance.fromJson(item.getAsJsonObject()));
      }
      snapshot.clear();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    
    return this;
  }
}