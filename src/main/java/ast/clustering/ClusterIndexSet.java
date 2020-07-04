package ast.clustering;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import persistence.Jsonify;

public class ClusterIndexSet implements Jsonify<ClusterIndexSet> {

  private String type;
  private List<ClusterIndex> elements;

  private Cluster parent;

  public ClusterIndexSet(String type, Cluster parent) {
    this.type = type;
    this.elements = new ArrayList<>();
    this.parent = parent;
  }

  @Override
  public JsonObject toJson() {
    
    this.elements.sort((a, b) -> {
      if(a.getSize() < b.getSize()) return 1;
      else if(a.getSize() > b.getSize()) return -1;
      return 0;
    });

    var object = new JsonObject();
    object.addProperty("type", type);

    var list = new JsonArray();
    for(var element: this.elements) {
      list.add(element.toJson());
    }

    object.add("elements", list);

    return object;
  }

  public void add(ClusterIndex item) {
    this.elements.add(item);
  }

  public List<ClusterIndex> getElements() {
    return this.elements;
  }

  public String getType() {
    return type;
  }

  @Override
  public ClusterIndexSet fromJson(JsonObject content) {
    //problema na leitura
    if(content != null) {
      this.type = content.get("type").getAsString();
      this.elements = new ArrayList<>();
    
      var items = content.get("elements").getAsJsonArray();
      for(var item: items) {
        var index = new ClusterIndex();
        index.setParent(parent);
        elements.add(index.fromJson(item.getAsJsonObject()));
      }
    }
    return this;
  }


}