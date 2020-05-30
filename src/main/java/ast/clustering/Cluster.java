package ast.clustering;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import ast.extractor.mapping.Mapping;
import core.Jsonify;

public class Cluster implements Jsonify<Cluster> {

  private String repository;
  private String branch;

  private List<ClusterIndex> items;

  private ClusterPersistor persistor;

  public Cluster() {
    this.persistor = new ClusterPersistor(repository);
    this.items = new ArrayList<>();
  }

  public Cluster(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
    this.items = new ArrayList<>();
    this.persistor = new ClusterPersistor(repository);
  }

  ClusterPersistor persistor() {
    return persistor;
  }

  public void addItem(Mapping mapping) {
    var item = new ClusterItem(mapping, items.size() + 1, this);
    persistor.item().write(item);

    var index = new ClusterIndex(item.getCode(), item.getPattern(), this);
    items.add(index);
    persistor.write(this);
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
    result.addProperty("items", items.toString());
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