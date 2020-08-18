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
import ast.findings.ClusterItemReader;
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

  private Integer contagemCommit;
  private Snapshot snapshot;
  private String base;
  private ClusterItemReader reader;

  public ClusterExtractor(String repository, String branch) throws GitAPIException {
    this.repository = repository;
    this.branch = branch;

    this.snapshot = Snapshot.create(this.repository, this.branch);
    ElementFactory.setTempRepository(this.repository);
    
    this.cluster = new ClusterSet(repository, branch);
    this.contagemCommit = 0;
    this.base = Settings.get().folderOfMetadata().getAbsolutePath();
    this.reader = new ClusterItemReader(this.cluster);
  }

  public void execute() {
    var manager = loadBaseFile();
    
    for (var entry : manager.entrySet()) {
      var commit = entry.getKey();
      System.err.println("> clustering commit #" + contagemCommit + " " + commit);
      for (var item : entry.getValue().entrySet()) {
        var file = item.getKey();
        var instance = getInstance(commit, file);
        if (instance != null) {
          identifyCluster(instance);
        }
      }
      
      //cluster.persistor().write();
      this.contagemCommit++;
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
          /*if(valor != 0) {
            commit.put(map.getKey(), valor);
          }*/
          commit.put(map.getKey(), valor);
        }
        
        //if(!commit.isEmpty()) {
        result.put(entry.getKey(), commit);
        //}
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

  private Index readElement(String code) {
    var numberCode = Integer.valueOf(code);
    return reader.getIndexElement(numberCode);
  }

  private void identifyCluster(MultiPattern instance) {

    var added = false;

    String type = null;
    try {
      type = instance.getType();
      if("UndefinedElement".equals(instance.getType())) return;
    } catch(Exception ex) {
      return;
    }

    var mapping = instance.getOverallMapping();
    var deleteList = new ArrayList<Integer>();
    
    for(var entry: cluster.getGroup(type).entrySet()) {
      var value = entry.getValue();
      if(value == null) {
        value = this.readElement(entry.getKey());
      }

      if (isSameTypeMappings(type, value.getBase().getOverallMapping())) {
        try {
          if (value.getPattern().matches(mapping)) {
            value.add(instance, this.contagemCommit);
            added = true;
            break;
          } 
        } catch(Exception ex) {
          System.out.println("> falha na comparação de multipatterns");
        } 
      }

      if(value.getSize() > 1) {
        cluster.getGroup(type).replace(value.getCode().toString(), value);
      } else if(value.getSize() == 1 && this.contagemCommit - value.getPosition() > 100) {
        deleteList.add(value.getCode());
      }
    }

    if (!added) {
      cluster.addItem(instance, this.contagemCommit);
    }

    for(var item: deleteList) {
      cluster.getGroup(type).replace(item.toString(), null);
    }
    System.gc();

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