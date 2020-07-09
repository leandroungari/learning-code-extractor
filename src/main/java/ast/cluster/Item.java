package ast.cluster;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ast.multipattern.MultiPattern;
import persistence.Jsonify;

public class Item implements Jsonify<Item> {

  private Integer code;
  private MultiPattern base;
  private ArrayList<Integer> items;

  private ClusterSet parent;

  public Item(Integer code, MultiPattern base, ClusterSet parent) {
    this.code = code;
    this.base = base;
    this.items = new ArrayList<>();
    this.parent = parent;
  }

  public void add(Integer code) {
    this.items.add(code);
  }

  public Integer getCode() {
    return code;
  }

  public MultiPattern getBase() {
    return base;
  }

  @Override
  public JsonObject toJson() {
    var object = new JsonObject();
    object.addProperty("code", code);
    object.add("base", base.toJson());

    var list = new JsonArray();
    for(var item: this.items) {
      list.add(item);
    }
    object.add("items", list);

    return object;
  }

  @Override
  public Item fromJson(JsonObject content) {
    this.code = content.get("code").getAsInt();

    this.items = new ArrayList<>();
    for(var a: content.get("items").getAsJsonArray()) {
      this.items.add(a.getAsInt());
    }

    if(base == null) {
      var item = new MultiPattern();
      item.setSnapshot(parent.getSnapshot());
      this.base = item.fromJson(content.get("base").getAsJsonObject());
    }

    return this;
  }


} 