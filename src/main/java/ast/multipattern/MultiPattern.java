package ast.multipattern;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ast.extractor.mapping.Mapping;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;
import ast.snapshot.Snapshot;
import persistence.Jsonify;

public class MultiPattern implements Jsonify<MultiPattern> {

  private Integer code;
  private List<String> commits;
  private Integer size;

  private List<Mapping> items;

  private MultiPatternProcessor processor;
  private Snapshot snapshot;

  public MultiPattern() {
  }

  public MultiPattern(Integer code, String commit, Mapping mapping) {
    this.code = code;
    this.commits = new ArrayList<>();
    this.commits.add(commit);
    this.size = 1;

    this.items = new ArrayList<>();
    this.items.add(mapping);
  }

  public void setSnapshot(Snapshot snapshot) {
    this.snapshot = snapshot;
  }

  public void add(String commit, Mapping mapping) {
    this.items.add(mapping);
    this.size++;
    this.commits.add(commit);
  }

  public void setProcessor(MultiPatternProcessor processor) {
    this.processor = processor;
  }

  public String getType() {
    return getLastMapping().source().type();
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public Integer getSize() {
    return this.size;
  }

  public String getLastCommit() {
    return commits.get(commits.size() - 1);
  }

  public String getFirstCommit() {
    return commits.get(0);
  }

  public Mapping getLastMapping() {
    return items.get(items.size() - 1);
  }

  public Mapping getFirstMapping() {
    return items.get(0);
  }

  public Mapping getOverallMapping() {

    if(items.size() == 1) return this.getLastMapping();

    var isUpdate = false;
    for(var item: this.items) {
      if(item instanceof UpdateMapping) {
        isUpdate = true;
        break;
      }
    }

    Mapping mapping = null;
    var before = this.getFirstMapping().source().get();
    var after = this.getLastMapping().destination().get();
    if(isUpdate) {
      mapping = new UpdateMapping(before, after);
    } else {
      mapping = new MoveMapping(before, after);
    }

    return mapping;
  }

  public Boolean compatibility(Mapping other) {
    try {
      var last = items.get(items.size() - 1);

      // criar verificação se o source e os dests de cada um são diferentes
      var lastSrcCode = last.source().dependencies().toString();
      var lastDestCode = last.destination().dependencies().toString();

      var otherSrcCode = other.source().dependencies().toString();
      var otherDestCode = other.destination().dependencies().toString();

      if (lastSrcCode.equals(otherSrcCode) && lastDestCode.equals(otherDestCode)) {

        return false;
      }

      var destinationPath = last.destination().path();
      var sourcePath = other.source().path();
      if (!sourcePath.equals(destinationPath)) {
        return false;
      }

      return lastDestCode.equals(otherSrcCode);
    } catch (Exception ex) {
      return false;
    }
  }

  @Override
  public JsonObject toJson() {
    var object = new JsonObject();
    object.addProperty("code", code);

    var items = new JsonArray();
    for (var commit : commits) {
      items.add(commit);
    }

    object.add("commits", items);

    object.addProperty("size", size);

    var list = new JsonArray(this.items.size());
    for (var item : this.items) {
      list.add(item.toJson());
    }
    object.add("items", list);
    return object;
  }

  @Override
  public MultiPattern fromJson(JsonObject content) {
    this.code = content.get("code").getAsInt();
    this.commits = new ArrayList<>();
    for (var item : content.get("commits").getAsJsonArray()) {
      this.commits.add(item.getAsString());
    }

    this.size = content.get("size").getAsInt();

    var position = 0;
    this.items = new ArrayList<>();
    for (var element : content.get("items").getAsJsonArray()) {
      if(snapshot != null) {
        this.items.add(Mapping.fromJson(
          element.getAsJsonObject(), 
          snapshot,
          this.commits.get(position))
        );
      } else {
        this.items.add(Mapping.fromJson(
          element.getAsJsonObject(), 
          processor.getSnapshot(), 
          this.commits.get(position))
        );
      }
      position++;
    }

    return this;
  }
}