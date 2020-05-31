package ast.clustering;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.Pattern;
import ast.TemplateBuilder;
import ast.extractor.mapping.Mapping;
import ast.snapshot.Snapshot;
import core.Jsonify;

public class ClusterItem implements Jsonify<ClusterItem> {

  private Integer code;
  private Pattern pattern;
  private List<Item> items;

  private Cluster parent;

  ClusterItem(Mapping mapping, String commit, Integer code, Cluster parent) {
    this.pattern = extractPattern(mapping);
    this.items = new ArrayList<>();
    this.code = code;
    this.parent = parent;
    
    this.add(mapping, commit);
  }

  ClusterItem(Cluster parent) {
    this.parent = parent;
  }

  public void add(Mapping mapping, String commit) {
    items.add(new Item(mapping, commit));
  }

  public Integer getCode() {
    return code;
  }

  public List<Item> getItems() {
    return items;
  }

  public Pattern getPattern() {
    return pattern;
  }

  private Pattern extractPattern(Mapping mapping) {
    var template = new TemplateBuilder<>(mapping);
    return template.model();
  }

  @Override
  public String toString() {
    return toJson().toString();
  }


  @Override
  public JsonObject toJson() {
    var result = new JsonObject();
    result.add("pattern", pattern.toJson());
    var list = new JsonArray();
    for(var item: items) {
      list.add(item.toJson());
    }
    result.add("items", list);
    return result;
  }

  @Override
  public ClusterItem fromJson(JsonObject content) {
    try {
      var snapshot = new Snapshot(parent.repository(), parent.branch());
      this.pattern = new Pattern().fromJson(content.get("pattern").getAsJsonObject());
      this.items = new ArrayList<>();
      for(var item: content.get("items").getAsJsonArray()) {
        var instance = new Item();
        instance.setSnapshot(snapshot);
        this.items.add(instance.fromJson(item.getAsJsonObject()));
      }
      snapshot.clear();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    
    return this;
  }
}