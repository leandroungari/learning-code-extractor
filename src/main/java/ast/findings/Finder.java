package ast.findings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.cluster.ClusterSet;
import ast.cluster.ClusterSetPersistor;
import ast.extractor.elements.ElementFactory;
import ast.snapshot.Snapshot;
import persistence.Settings;
import persistence.resource.PlainText;
import persistence.resource.Resource;

public class Finder {

  private String origin;
  private String branchOrigin;
  private ClusterSet clusterOrigin;

  private String target;
  private String branchTarget;
  private ClusterSet clusterTarget;

  private static final String[] categories = new String[] { "annotationtype", "class", "constructor", "enum", "field",
      "interface", "method", "statement", "temp", "undefined" };

  public Finder(String repository, String branch) throws GitAPIException {
    Snapshot.create(repository, branch);
    
    this.origin = repository;
    this.branchOrigin = branch;
    this.clusterOrigin = getClusterOrigin();
  }

  private ClusterSet getClusterOrigin() {
    var cluster = new ClusterSet(origin, branchOrigin);
    var persistor = new ClusterSetPersistor(cluster);
    return persistor.read();
  }

  public void setRepositoryTarget(String repository, String branch) throws GitAPIException {
    Snapshot.create(repository, branch);

    //TODO 
    //adicionar no finder as mesmas configurações 
    //do clustering com ajustes para funcionar
    this.target = repository;
    this.branchTarget = branch;
    this.clusterTarget = getClusterTarget();
  }

  private ClusterSet getClusterTarget() {
    var cluster = new ClusterSet(target, branchTarget);
    var persistor = new ClusterSetPersistor(cluster);
    return persistor.read();
  }

  private void process(String category, HashMap<String, List<Integer>> result) {
    ElementFactory.setTempRepository(origin);
    for(var index: clusterOrigin.getItems(category)) {
      var patternOrigin = index.getPattern();
      ElementFactory.setTempRepository(target);
      for(var outer: clusterTarget.getItems(category)) {
        var instance = outer.getBase().getOverallMapping();
        if(patternOrigin.matches(instance)) {
          
          if(result.containsKey("#" + index.getCode())) {
            result.get("#" + index.getCode()).add(outer.getCode());
          } else {
            var list = new ArrayList<Integer>();
            list.add(outer.getCode());
            result.put("#" + index.getCode(), list);
          }
        }
      }
      ElementFactory.setTempRepository(origin);
    }
  }

  public Map<String, List<Integer>> compatibility() {
    var result = new HashMap<String, List<Integer>>();
    
    for(var category: categories) {
      this.process(category, result);
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