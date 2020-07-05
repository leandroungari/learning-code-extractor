package ast.cluster;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import persistence.Jsonify;

public class ClusterKeySet implements Jsonify<ClusterKeySet> {

  private String type;
  private List<ClusterKey> elements;

  private ClusterSet parent;

  public ClusterKeySet(String type, ClusterSet parent) {
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

  public void add(ClusterKey item) {
    this.elements.add(item);
  }

  public List<ClusterKey> getElements() {
    return this.elements;
  }

  public String getType() {
    return type;
  }

  @Override
  public ClusterKeySet fromJson(JsonObject content) {
    //problema na leitura
    if(content != null) {
      this.type = content.get("type").getAsString();
      this.elements = new ArrayList<>();
    
      var items = content.get("elements").getAsJsonArray();
      for(var item: items) {
        var index = new ClusterKey();
        index.setParent(parent);
        elements.add(index.fromJson(item.getAsJsonObject()));
      }
    }
    return this;
  }


}