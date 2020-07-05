package ast.cluster;

import com.google.gson.JsonObject;

import ast.multipattern.MultiPattern;
import ast.template.Template;
import ast.template.TemplateBuilder;
import persistence.Jsonify;

public class ClusterKey implements Jsonify<ClusterKey>, Comparable<ClusterKey> {

  private Integer code;
  private MultiPattern base;
  private Integer size;

  private ClusterSet parent;

  public ClusterKey() {
    this.size = 1;
  }

  public ClusterKey(Integer code, MultiPattern instance, ClusterSet parent) {
    this.code = code;
    this.base = instance;
    this.size = 1;
    this.parent = parent;
  }

  public Integer getSize() {
    return size;
  }

  public void add(MultiPattern instance) {
    var clusterItem = parent.persistor().item().read(code);
    clusterItem.add(instance);
    parent.persistor().item().write(clusterItem);
    this.size = clusterItem.getItems().size();
    parent.writeClusterIndexSet();
  }

  public Integer getCode() {
    return code;
  }

  public Template getPattern() {
    var builder = new TemplateBuilder(base.getOverallMapping());
    return builder.create();
  }

  public MultiPattern getBase() {
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

  public void setParent(ClusterSet cluster) {
    this.parent = cluster;
  }

  @Override
  public ClusterKey fromJson(JsonObject content) {
    this.code = content.get("code").getAsInt();
    
    var item = new MultiPattern();
    item.setSnapshot(parent.getSnapshot());
    this.base = item.fromJson(content.get("base").getAsJsonObject());
    this.size = Integer.valueOf(content.get("size").getAsInt());
    return this;
  }

  @Override
  public int compareTo(ClusterKey other) {
    if(this.size < other.size) {
      return 1;
    } else if(this.size > other.size) {
      return -1;
    }
    return 0;
  }

}