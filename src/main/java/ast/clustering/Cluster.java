package ast.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ast.extractor.mapping.Mapping;
import ast.snapshot.Snapshot;
import persistence.Jsonify;

public class Cluster implements Jsonify<Cluster> {

  private String repository;
  private String branch;
  private Snapshot snapshot;

  private HashMap<String,List<ClusterIndex>> items;

  private ClusterPersistor persistor;

  public Cluster() {
    this.persistor = new ClusterPersistor(this);
    this.items = new HashMap<>();
  }

  public Cluster(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
    this.items = new HashMap<>();
    this.persistor = new ClusterPersistor(this);
    this.snapshot = Snapshot.get(repository);
  }

  ClusterPersistor persistor() {
    return persistor;
  }

  private Integer getTotal() {
    var size = 0;
    for(var item: items.values()) {
      size += item.size();
    }
    return size;
  }

  private String transformToKey(String type) {
    return type.replace("Element", "").toLowerCase();
  }

  public void addItem(Mapping mapping, String commit) {
    var type = mapping.source().type();
    var key = transformToKey(type);

    var position = getTotal() + 1;
    var item = new ClusterItem(mapping, commit, position, this);
    persistor.item().write(item);

    var index = new ClusterIndex(position, mapping, commit, this);
    
    var list = items.get(key);
    if(list == null) {
      list = new ArrayList<ClusterIndex>();
      items.put(key, list);
    }
    list.add(index);

    Collections.sort(list);
    persistor.write();
  }

  String repository() {
    return repository;
  }

  String branch() {
    return branch;
  }

  Snapshot getSnapshot() {
    return snapshot;
  }

  public List<ClusterIndex> getItems(String type) {
    var key = transformToKey(type);
    var instance = items.get(key);
    if(instance == null) {
      instance = new ArrayList<>();
      items.put(key, instance);
    }
    return instance;
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
    var conjunto = new JsonObject();
    for (var entry : items.entrySet()) {
      var list = entry.getValue();
      var listagem = new JsonArray();
      for(var item: list) {
        listagem.add(item.toJson());
      }
      conjunto.add(entry.getKey(), listagem);
    }
    result.add("items", conjunto);
    return result;
  }

  @Override
  public Cluster fromJson(JsonObject content) {
    this.repository = content.get("repository").getAsString();
    this.branch = content.get("branch").getAsString();
    this.items = new HashMap<>();

    var conjunto = content.get("items").getAsJsonObject();
    for(var entry: conjunto.entrySet()) {
      var listagem = entry.getValue().getAsJsonArray();
      var list = new ArrayList<ClusterIndex>();
      for(var item: listagem) {
        list.add(new ClusterIndex().fromJson(item.getAsJsonObject()));
      }

      items.put(entry.getKey(), list);
    }
    return this;
  }
}