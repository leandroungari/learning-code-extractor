package ast.cluster;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ast.extractor.elements.ElementFactory;
import ast.multipattern.MultiPattern;
import persistence.Jsonify;

public class ClusterItem implements Jsonify<ClusterItem> {

  private Integer code;
  private MultiPattern base;
  private List<MultiPattern> items;

  private ClusterSet parent;

  ClusterItem(MultiPattern instance, Integer code, ClusterSet parent) {
    this.base = instance;
    this.items = new ArrayList<>();
    this.code = code;
    this.parent = parent;

    this.add(instance);
  }

  ClusterItem(Integer code, ClusterSet parent) {
    this.parent = parent;
    this.code = code;
  }

  public void add(MultiPattern instance) {
    items.add(instance);
  }

  public Integer getCode() {
    return code;
  }

  public List<MultiPattern> getItems() {
    return items;
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
    ElementFactory.enableLoadElement();
    result.add("base", base.toJson());
    ElementFactory.disableLoadElement();
    var list = new JsonArray();
    for (var item : items) {
      list.add(item.toJson());
    }
    result.add("items", list);
    return result;
  }

  @Override
  public ClusterItem fromJson(JsonObject content) {

    var base = new MultiPattern();
    base.setSnapshot(parent.getSnapshot());

    this.base = base.fromJson(content.get("base").getAsJsonObject());
    this.items = new ArrayList<>();
    for (var item : content.get("items").getAsJsonArray()) {
      var instance = new MultiPattern();
      instance.setSnapshot(parent.getSnapshot());
      this.items.add(instance.fromJson(item.getAsJsonObject()));
    }
    return this;
  }
}