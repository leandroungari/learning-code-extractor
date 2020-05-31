package ast.clustering;

import com.google.gson.JsonObject;

import ast.Pattern;
import ast.extractor.mapping.Mapping;
import core.Jsonify;

public class ClusterIndex implements Jsonify<ClusterIndex> {

  private Integer code;
  private Pattern pattern;
  private Integer size;

  private Cluster parent;

  public ClusterIndex() {
    this.size = 1;
  }

  public ClusterIndex(Integer code, Pattern pattern, Cluster parent) {
    this.code = code;
    this.pattern = pattern;
    this.size = 1;
    this.parent = parent;
  }

  public void add(Mapping mapping, String commit) {
    var clusterItem = parent.persistor().item().read(code);
    clusterItem.add(mapping, commit);
    parent.persistor().item().write(clusterItem);
    this.size = clusterItem.getItems().size();
  }

  public Integer getCode() {
    return code;
  }

  public Pattern getPattern() {
    return pattern;
  }

  @Override
  public String toString() {
    return toJson().toString();
  }


  @Override
  public JsonObject toJson() {
    var result = new JsonObject();
    result.addProperty("code", code);
    result.add("pattern", pattern.toJson());
    result.addProperty("size", size);
    return result;
  }

  @Override
  public ClusterIndex fromJson(JsonObject content) {
    this.code = content.get("code").getAsInt();
    this.pattern = new Pattern().fromJson(content.get("pattern").getAsJsonObject());
    this.size = Integer.valueOf(content.get("size").getAsInt());
    return this;
  }

}