package ast.clustering;

import com.google.gson.JsonObject;

import ast.extractor.mapping.Mapping;
import ast.snapshot.Snapshot;
import ast.template.Template;
import ast.template.TemplateBuilder;
import persistence.Jsonify;

public class ClusterIndex implements Jsonify<ClusterIndex>, Comparable<ClusterIndex> {

  private Integer code;
  private Item base;
  private Integer size;

  private Cluster parent;

  private Snapshot snap;

  public ClusterIndex() {
    this.size = 1;
  }

  public ClusterIndex(Integer code, Mapping base, String commit, Cluster parent) {
    this.code = code;
    this.base = new Item(commit, base);
    this.size = 1;
    this.parent = parent;
  }

  public void add(Mapping mapping, String commit) {
    var clusterItem = parent.persistor().item().read(code);
    clusterItem.add(mapping, commit);
    parent.persistor().item().write(clusterItem);
    this.size = clusterItem.getItems().size();
  }

  public void setSnapshot(Snapshot snap) {
    this.snap = snap;
  }

  public Integer getCode() {
    return code;
  }

  public Template getPattern() {
    var builder = new TemplateBuilder(base.getMapping());
    return builder.create();
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
    result.addProperty("code", code);
    result.add("base", base.toJson());
    result.addProperty("size", size);
    return result;
  }

  @Override
  public ClusterIndex fromJson(JsonObject content) {
    this.code = content.get("code").getAsInt();
    
    var item = new Item();
    item.setSnapshot(snap);
    this.base = item.fromJson(content.get("base").getAsJsonObject());
    this.size = Integer.valueOf(content.get("size").getAsInt());
    return this;
  }

  @Override
  public int compareTo(ClusterIndex other) {
    if(this.size < other.size) {
      return 1;
    } else if(this.size > other.size) {
      return -1;
    }
    return 0;
  }

}