package ast.clustering;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ast.extractor.mapping.Mapping;
import persistence.Jsonify;

public class Cluster implements Jsonify<Cluster> {

  private String repository;
  private String branch;

  private List<ClusterIndex> items;

  private ClusterPersistor persistor;

  public Cluster() {
    this.persistor = new ClusterPersistor(this);
    this.items = new ArrayList<>();
  }

  public Cluster(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
    this.items = new ArrayList<>();
    this.persistor = new ClusterPersistor(this);
  }

  ClusterPersistor persistor() {
    return persistor;
  }

  public void addItem(Mapping mapping, String commit) {
    var item = new ClusterItem(mapping, commit, items.size() + 1, this);
    persistor.item().write(item);

    var index = new ClusterIndex(item.getCode(), mapping, commit, this);
    items.add(index);
    persistor.write();
  }

  String repository() {
    return repository;
  }

  String branch() {
    return branch;
  }

  public List<ClusterIndex> getItems() {
    return items;
  }

  @Override
  public String toString() {
    return toJson().toString();
  }


  @Override
  public JsonObject toJson() {
    var result = new JsonObject();
    result.addProperty("repository", repository);
    result.addProperty("branch", branch);
    var list = new JsonArray();
    for(var item: items) {
      list.add(item.toJson());
    }
    result.add("items", list);
    return result;
  }

  @Override
  public Cluster fromJson(JsonObject content) {
    this.repository = content.get("repository").getAsString();
    this.branch = content.get("branch").getAsString();
    this.items = new ArrayList<>();
    var list = content.get("items").getAsJsonArray();
    for(var item: list) {
      this.items.add(new ClusterIndex().fromJson(item.getAsJsonObject()));
    }
    return this;
  }
}