package ast.clustering;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonObject;

import ast.extractor.mapping.Mapping;
import ast.snapshot.Snapshot;
import persistence.Jsonify;
import persistence.Settings;
import persistence.resource.JSON;
import persistence.resource.Resource;

public class Cluster implements Jsonify<Cluster> {

  private String repository;
  private String branch;
  private Snapshot snapshot;

  // private HashMap<String,List<ClusterIndex>> items;
  private ClusterIndexSet currentSet;

  private ClusterPersistor persistor;
  private Integer counter = 0;
  private String base;

  public Cluster() {
    this.persistor = new ClusterPersistor(this);
    // this.items = new HashMap<>();
  }

  public Cluster(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;
    // this.items = new HashMap<>();
    this.persistor = new ClusterPersistor(this);
    this.snapshot = Snapshot.get(repository);
    persistor.write();

    this.base = Settings.get().folderOfMetadata() + "/" + repository + "/" + "clusters";
  }

  ClusterPersistor persistor() {
    return persistor;
  }

  /*
   * private Integer getTotal() { var size = 0; for(var item: items.values()) {
   * size += item.size(); } return size; }
   */

  private String transformToKey(String type) {
    return type.replace("Element", "").toLowerCase();
  }

  public void addItem(Mapping mapping, String commit) {
    var type = mapping.source().type();
    var key = transformToKey(type);

    var position = ++counter;
    var item = new ClusterItem(mapping, commit, position, this);
    persistor.item().write(item);

    currentSet = this.readClusterIndexSet(key);
    var index = new ClusterIndex(position, mapping, commit, this);
    currentSet.add(index);
    this.writeClusterIndexSet();

    /*var list = items.get(key);
    if (list == null) {
      list = new ArrayList<ClusterIndex>();
      items.put(key, list);
    }
    list.add(index);*/

  }

  ClusterIndexSet readClusterIndexSet(String key) {
    System.err.println("> read clusterindex #index-" + key);
    if(currentSet != null && key.equals(currentSet.getType())) {
      return currentSet;
    }
    var resource = new Resource(
      this.base, 
      "#index-" + key + ".json"
    );
    currentSet = new ClusterIndexSet(key, this);
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

  public List<ClusterIndex> getItems(String type) {
    var key = transformToKey(type);
    /*var instance = items.get(key);
    if(instance == null) {
      instance = new ArrayList<>();
      items.put(key, instance);
    }
    return instance;*/
    var set = readClusterIndexSet(key);
    return set.getElements();
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
  public Cluster fromJson(JsonObject content) {
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