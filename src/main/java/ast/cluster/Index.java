package ast.cluster;


import com.google.gson.JsonObject;

import ast.multipattern.MultiPattern;
import ast.template.Template;
import ast.template.TemplateBuilder;
import persistence.Jsonify;

public class Index implements Comparable<Index>, Jsonify<Index> {

  private Integer code;
  private MultiPattern base;
  private Integer size;

  private ClusterSet parent;
  private ItemPersistor persistor;
  private Integer position;

  public Index(Integer code, MultiPattern base, ClusterSet parent, Integer counter) {
    this.code = code;
    this.base = base;
    this.size = 1;
    this.parent = parent;
    this.persistor = new ItemPersistor(this.parent);
    this.position = counter;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public Integer getPosition() {
    return position;
  }

  

  public Index(ClusterSet parent) {
    this.parent = parent;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public void add(MultiPattern item, Integer position) {

    var clusterItem = new Item(this.code, this.base, parent);
    try {
      clusterItem = persistor.read(clusterItem);
      clusterItem.add(item.getCode());
      persistor.write(clusterItem);
      this.size++;
      this.setPosition(position);
    } catch(Exception ex) {
      //pula o cluster item
    }

  }

  public Integer getCode() {
    return code;
  }

  public MultiPattern getBase() {
    return base;
  }

  public Integer getSize() {
    return size;
  }

  public Template getPattern() {
    var builder = new TemplateBuilder(base.getOverallMapping());
    return builder.create();
  }

  @Override
  public int compareTo(Index other) {
    if (this.size < other.size)
      return 1;
    else if (this.size > other.size)
      return -1;
    return 0;
  }

  @Override
  public JsonObject toJson() {
    var object = new JsonObject();
    object.addProperty("code", code);
    object.addProperty("size", size);
    object.add("base", base.toJson());

    return object;
  }

  @Override
  public Index fromJson(JsonObject content) {
    this.code = content.get("code").getAsInt();
    this.size = content.get("size").getAsInt();

    var item = new MultiPattern();
    item.setSnapshot(parent.getSnapshot());
    this.base = item.fromJson(content.get("base").getAsJsonObject());

    return this;
  }


}