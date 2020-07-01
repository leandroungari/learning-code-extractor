package ast.multipattern;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ast.extractor.mapping.Mapping;
import persistence.Jsonify;

public class MultiPattern implements Jsonify<MultiPattern> {

  private Integer code;
  private List<String> commits;
  private Integer size;

  private List<Mapping> items;

  private MultiPatternProcessor processor;

  MultiPattern() {}

  public MultiPattern(Integer code, String commit, Mapping mapping) {
    this.code = code;
    this.commits = new ArrayList<>();
    this.commits.add(commit);
    this.size = 1;

    this.items = new ArrayList<>();
    this.items.add(mapping);
  }

  public void add(String commit, Mapping mapping) {
    this.items.add(mapping);
    this.size++;
    this.commits.add(commit);
  }

  public void setProcessor(MultiPatternProcessor processor) {
    this.processor = processor;
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
    return commits.get(commits.size()-1);
  }


  public Boolean compatibility(Mapping other) {
    var last = items.get(items.size()-1);

    //criar verificação se o source e os dests de cada um são diferentes
    var lastSrcCode = last.source().dependencies().toString();
    var lastDestCode = last.destination().dependencies().toString();

    var otherSrcCode = other.source().dependencies().toString();
    var otherDestCode = other.destination().dependencies().toString();

    if(lastSrcCode.equals(otherSrcCode) &&
       lastDestCode.equals(otherDestCode)) {
      
      return false;
    }
  
    var destinationPath = last.destination().path();
    var sourcePath = other.source().path();
    if(!sourcePath.equals(destinationPath)) {
      return false;
    }
    
    
    return lastDestCode.equals(otherSrcCode);
  }

  @Override
  public JsonObject toJson() {
    var object = new JsonObject();
    object.addProperty("code", code);

    var items = new JsonArray();
    for(var commit: commits) {
      items.add(commit);
    }

    object.add("commits", items);
    
    object.addProperty("size", size);

    var list = new JsonArray(this.items.size());
    for(var item: this.items) {
      list.add(item.toJson());
    }
    object.add("items", list);
    return object;
  }

  @Override
  public MultiPattern fromJson(JsonObject content) {
    this.code = content.get("code").getAsInt();
    this.commits = new ArrayList<>();
    for(var item: content.get("commits").getAsJsonArray()) {
      this.commits.add(item.getAsString());
    }

    this.size = content.get("size").getAsInt();

    var position = 0;
    this.items = new ArrayList<>();
    for(var element: content.get("items").getAsJsonArray()) {
      this.items.add(Mapping.fromJson(
        element.getAsJsonObject(), 
        processor.getSnapshot(),
        this.commits.get(position) 
      ));
      position++;
    }

    return this;
  }
}