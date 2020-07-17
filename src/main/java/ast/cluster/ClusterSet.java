package ast.cluster;

import java.util.HashMap;
import java.util.PriorityQueue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ast.multipattern.MultiPattern;
import ast.snapshot.Snapshot;
import persistence.Jsonify;

public class ClusterSet implements Jsonify<ClusterSet> {

  private String repository;
  private String branch;
  private Snapshot snapshot;

  private HashMap<String, PriorityQueue<Index>> groups;
  
  private ClusterSetPersistor persistor;
  
  private Integer counter = 0;

  public ClusterSet() {
    this.persistor = new ClusterSetPersistor(this);
    this.groups = new HashMap<>();
  }

  public ClusterSet(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
    this.groups = new HashMap<>();

    this.persistor = new ClusterSetPersistor(this);
    this.snapshot = Snapshot.get(repository);
    persistor.write();

  }

  public ClusterSetPersistor persistor() {
    return persistor;
  }

  private String transformToKey(String type) {
    return type.replace("Element", "").toLowerCase();
  }

  public PriorityQueue<Index> getGroup(String type) {
    var name = this.transformToKey(type);

    var item = this.groups.get(name);
    if(item == null) {
      item = new PriorityQueue<>();
      this.groups.put(name, item);
    }
    return item;
  }

  public void addItem(MultiPattern instance, Integer i) {
    var type = instance.getType();
    var key = transformToKey(type);

    var position = ++counter;

    var index = new Index(position, instance, this, i);
    this.createItemFile(position, instance);
    this.groups.get(key).add(index);
  }

  private void createItemFile(Integer code, MultiPattern base) {
    var item = new Item(code, base, this);
    item.add(base.getCode());

    var itemPersistor = new ItemPersistor(this);
    itemPersistor.write(item);
  }

  public String getRepository() {
    return repository;
  }

  String getBranch() {
    return branch;
  }

  Snapshot getSnapshot() {
    return snapshot;
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
    
    var instance = new JsonObject();
    for(var entry: this.groups.entrySet()) {
      var type = entry.getKey();
      
      var list = new JsonArray();
      for(var item: entry.getValue()) {
        list.add(item.toJson());
      }

      instance.add(type, list);
    }

    result.add("groups", instance);
    return result;
  }

  @Override
  public ClusterSet fromJson(JsonObject content) {
    this.repository = content.get("repository").getAsString();
    this.branch = content.get("branch").getAsString();
    
    this.groups = new HashMap<>();
    for(var entry: content.get("groups").getAsJsonObject().entrySet()) {
      var type = entry.getKey();

      var list = new PriorityQueue<Index>();
      for(var item: entry.getValue().getAsJsonArray()) {
        var index = new Index(this);
        list.add(index.fromJson(item.getAsJsonObject()));
      }
      this.groups.put(type, list);
    }

    return this;
  }
}