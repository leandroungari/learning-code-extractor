package ast.findings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ast.clustering.Cluster;
import ast.clustering.ClusterPersistor;
import persistence.Settings;
import persistence.resource.PlainText;
import persistence.resource.Resource;

public class Finder {

  private String origin;
  private String branchOrigin;
  private Cluster clusterOrigin;
  
  private String target;
  private String branchTarget;
  private Cluster clusterTarget;
  
  public Finder(String repository, String branch) {
    this.origin = repository;
    this.branchOrigin = branch;
    this.clusterOrigin = getClusterOrigin();
  }

  private Cluster getClusterOrigin() {
    var cluster = new Cluster(origin, branchOrigin);
    var persistor = new ClusterPersistor(cluster);
    return persistor.read();
  }

  public void setRepositoryTarget(String repository, String branch) {
    this.target = repository;
    this.branchTarget = branch;
    this.clusterTarget = getClusterTarget();
  }

  private Cluster getClusterTarget() {
    var cluster = new Cluster(target, branchTarget);
    var persistor = new ClusterPersistor(cluster);
    return persistor.read();
  }

  public Map<String, List<Integer>> compatibility() {
    var result = new HashMap<String, List<Integer>>();
    for(var index: clusterOrigin.getItems()) {
      var patternOrigin = index.getPattern();
      for(var outer: clusterTarget.getItems()) {
        var mapping = outer.getBase().getMapping();
        if(patternOrigin.matches(mapping)) {
          
          if(result.containsKey("#" + index.getCode())) {
            result.get("#" + index.getCode()).add(outer.getCode());
          } else {
            var list = new ArrayList<Integer>();
            list.add(outer.getCode());
            result.put("#" + index.getCode(), list);
          }
        }
      }
    }
    return result;
  }

  public void toStore(Map<String, List<Integer>> data) {
    var base = Settings.get()
      .folderOfMetadata() + "/" + 
      this.origin + "/findings";

    var resource = new Resource(base, "#" + target + ".json");
    var result = new JsonObject();
    result.addProperty("origin", origin);
    result.addProperty("target", target);
    
    var clusters = new JsonObject();
    for(var entry: data.entrySet()) {
      var list = new JsonArray();
      for(var item: entry.getValue()) {
        list.add(item);
      }
      clusters.add(entry.getKey(), list);
    }

    result.add("clusters", clusters);

    try {
      resource.write(new PlainText(result.toString()), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}