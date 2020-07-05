package ast.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import ast.multipattern.MultiPattern;
import ast.snapshot.Snapshot;
import persistence.Jsonify;
import persistence.Settings;
import persistence.resource.JSON;
import persistence.resource.Resource;

public class ClusterSet implements Jsonify<ClusterSet> {

  private String repository;
  private String branch;
  private Snapshot snapshot;

  // private HashMap<String,List<ClusterIndex>> items;
  private ClusterKeySet currentSet;

  private ClusterSetPersistor persistor;
  private Integer counter = 0;
  private String base;

  public ClusterSet() {
    this.persistor = new ClusterSetPersistor(this);
    // this.items = new HashMap<>();
  }

  public ClusterSet(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
    // this.items = new HashMap<>();
    this.persistor = new ClusterSetPersistor(this);
    this.snapshot = Snapshot.get(repository);
    persistor.write();

    this.base = Settings.get().folderOfMetadata() + "/" + repository + "/" + "clusters";
  }

  ClusterSetPersistor persistor() {
    return persistor;
  }

  /*
   * private Integer getTotal() { var size = 0; for(var item: items.values()) {
   * size += item.size(); } return size; }
   */

  private String transformToKey(String type) {
    return type.replace("Element", "").toLowerCase();
  }

  public void addItem(MultiPattern instance) {
    var type = instance.getType();
    var key = transformToKey(type);

    var position = ++counter;
    var item = new ClusterItem(instance, position, this);
    persistor.item().write(item);

    currentSet = this.readClusterIndexSet(key);
    var index = new ClusterKey(position, instance, this);
    currentSet.add(index);
    this.writeClusterIndexSet();

    /*var list = items.get(key);
    if (list == null) {
      list = new ArrayList<ClusterIndex>();
      items.put(key, list);
    }
    list.add(index);*/

  }

  ClusterKeySet readClusterIndexSet(String key) {
    System.err.println("> read clusterindex #index-" + key);
    if(currentSet != null && key.equals(currentSet.getType())) {
      return currentSet;
    }
    var resource = new Resource(
      this.base, 
      "#index-" + key + ".json"
    );
    currentSet = new ClusterKeySet(key, this);
    try {
      return resource.read(new JSON<>(currentSet));
    } catch (IOException e) {
      return currentSet;
    }
  }

  void writeClusterIndexSet() {
    System.err.println("> write clusterindex #index-" + currentSet.getType());
    var resource = new Resource(
      this.base, 
      "#index-" + currentSet.getType() + ".json"
    );
    try {
      resource.write(new JSON<>(currentSet), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
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

  public List<ClusterKey> getItems(String type) {
    var key = transformToKey(type);
    
    try {
      var set = readClusterIndexSet(key);
      return set.getElements();
    } catch(Exception ex) {
      return new ArrayList<>();
    }
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
    /*
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
    */
    return result;
  }

  @Override
  public ClusterSet fromJson(JsonObject content) {
    this.repository = content.get("repository").getAsString();
    this.branch = content.get("branch").getAsString();
    /*
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
    */
    return this;
  }
}