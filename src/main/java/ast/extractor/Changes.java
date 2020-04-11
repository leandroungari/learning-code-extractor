package ast.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ast.Mappable;

public class Changes implements Iterable<ChangeItem>, Mappable {

  private String oldPath;
  private String newPath;
  private List<ChangeItem> changes;

  Changes() {
    changes = new ArrayList<>();
  }

  public void add(ChangeItem change) {

    changes.add(change);
  }

  public ChangeItem first() {

    return changes.get(0);
  }

  public ChangeItem last() {

    return changes.get(size() - 1);
  }

  public ChangeItem get(Integer position) {

    return changes.get(position);
  }

  public Integer size() {
    return changes.size();
  }

  public void setNewPath(String newPath) {
    this.newPath = newPath;
  }

  public void setOldPath(String oldPath) {
    this.oldPath = oldPath;
  }

  public String getNewPath() {
    return newPath;
  }

  public String getOldPath() {
    return oldPath;
  }

  @Override
  public Iterator<ChangeItem> iterator() {

    return changes.iterator();
  }

  public JsonElement toMap() {
    var json = new JsonObject();
    json.addProperty("oldPath", oldPath);
    json.addProperty("newPath", newPath);
    
    var list = new JsonArray();
    for(var changeItem: this.changes) {
      list.add(changeItem.toMap());
    }
    json.add("result",list);
    return json;
  }

}