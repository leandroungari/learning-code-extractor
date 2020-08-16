package ast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.cluster.ClusterSet;
import ast.cluster.Index;
import ast.extractor.elements.ElementFactory;
import ast.extractor.mapping.Mapping;
import ast.multipattern.MultiPattern;
import ast.snapshot.Snapshot;
import persistence.Settings;
import persistence.resource.JSON;
import persistence.resource.PlainText;
import persistence.resource.Resource;

public class ClusterExtractor {

  private String repository;
  private String branch;

  private ClusterSet cluster;

  private Integer counter;
  private Snapshot snapshot;
  private String base;

  public ClusterExtractor(String repository, String branch) throws GitAPIException {
    this.repository = repository;
    this.branch = branch;

    this.snapshot = Snapshot.create(this.repository, this.branch);
    ElementFactory.setTempRepository(this.repository);
    
    this.cluster = new ClusterSet(repository, branch);
    this.counter = 0;
    this.base = Settings.get().folderOfMetadata().getAbsolutePath();
  }

  public void execute() {
    var manager = loadBaseFile();
    
    for (var entry : manager.entrySet()) {
      var commit = entry.getKey();
      System.err.println("> clustering commit #" + counter + " " + commit);
      for (var item : entry.getValue().entrySet()) {
        var file = item.getKey();
        var instance = getInstance(commit, file);
        if (instance != null) {
          identifyCluster(instance, counter);
        }
      }
      
      cluster.persistor().write();
      counter++;
    }
  }

  private MultiPattern getInstance(String commit, String file) {
    var resource = new Resource(this.base + "/" + repository + "/multipattern/" + commit, file + ".json");
    try {
      var instance = new MultiPattern();
      instance.setSnapshot(snapshot);
      return resource.read(new JSON<>(instance));
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }

  private Map<String, Map<String, Integer>> loadBaseFile() {
    var resource = new Resource(this.base + "/" + repository, "analise-multipattern.json");
    try {
      var data = resource.read(new PlainText(new String()));
      var content = new Gson().fromJson(data, JsonObject.class);

      var result = new HashMap<String, Map<String, Integer>>();
      content.entrySet().forEach(entry -> {

        var commit = new HashMap<String, Integer>();
  
        for (var map : entry.getValue().getAsJsonObject().entrySet()) {
          var valor = map.getValue().getAsInt();
          if(valor != 0) {
            commit.put(map.getKey(), valor);
          }
        }
        
        if(!commit.isEmpty()) {
          result.put(entry.getKey(), commit);
        }
      });

      return result;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  private Boolean isSameTypeMappings(String type, Mapping base) {
    String srcType = "", destType = "";

    var srcElement = base.source();
    var destElement = base.destination();
    
    if(srcElement != null) {
      srcType = srcElement.type();
    }

    if(destElement != null) {
      destType = destElement.type();
    }

    return (srcType.equals(type) || destType.equals(type));
  }

  private void identifyCluster(MultiPattern instance, Integer position) {

    var added = false;

    String type = null;
    try {
      type = instance.getType();
      if("UndefinedElement".equals(instance.getType())) return;
    } catch(Exception ex) {
      return;
    }

    var mapping = instance.getOverallMapping();
    //var deleteList = new ArrayList<Index>();
    
    for(var item: cluster.getGroup(type)) {
      if (isSameTypeMappings(type, item.getBase().getOverallMapping())) {
        try {
          if (item.getPattern().matches(mapping)) {
            item.add(instance, position);
            added = true;
            break;
          } 
        } catch(Exception ex) {
          System.out.println("> falha na comparação de multipatterns");
        } 
      }
      
      /*if(item.getSize() == 1 && position - item.getPosition() > 100) {
        deleteList.add(item);
      }*/
    }

    if (!added) {
      cluster.addItem(instance, position);
    }

    /*if(!deleteList.isEmpty()){
      cluster.getGroup(type).removeAll(deleteList);
    }*/

    /*var elements = cluster.getItems(type);
    var mapping = instance.getOverallMapping();
    
    for (var item : elements) {
      if (isSameTypeMappings(type, item.getBase().getOverallMapping())) {
        try {
          if (item.getPattern().matches(mapping)) {
            item.add(instance);
            added = true;
            break;
          }
        } catch (SpoonException ex) {
          // System.err.println(ex.getMessage());
        }
      }
    }
    if (!added) {
      cluster.addItem(instance);
    }*/
  }
}