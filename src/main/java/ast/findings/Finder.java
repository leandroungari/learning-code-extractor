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
import ast.cluster.Index;
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

  public Finder(String repository, String branch) throws GitAPIException {
    Snapshot.create(repository, branch);

    this.origin = repository;
    this.branchOrigin = branch;
    this.clusterOrigin = new ClusterSet(origin, branchOrigin);
  }

  public void setRepositoryTarget(String repository, String branch) throws GitAPIException {
    Snapshot.create(repository, branch);

    this.target = repository;
    this.branchTarget = branch;
    this.clusterTarget = new ClusterSet(target, branchTarget);
  }

  private void process(HashMap<String, List<Integer>> result) {
    var originReader = new ClusterItemReader(clusterOrigin);
    var targetReader = new ClusterItemReader(clusterTarget);

    try {
      var clusterListOrigin = originReader.getListOfClusters();
      var clusterListTarget = targetReader.getListOfClusters();

      var map = new HashMap<Integer, Index>();
      var counter = 0;

      ElementFactory.setTempRepository(origin);
      for (var i = 0; i < clusterListOrigin.size();) {
        ++counter;
        var indexCode = clusterListOrigin.get(i);
        var index = originReader.getIndexElement(indexCode);
        try {
          var patternOrigin = index.getPattern();
          ElementFactory.setTempRepository(target);
          var adicionado = false;
          for (var j = 0; j < clusterListTarget.size(); j++) {
            var outerCode = clusterListTarget.get(j);
            System.err.println("> Verificando recorrência do cluster " + origin + " " + indexCode + " (" + outerCode
                + ") #" + counter);

            Index outer = null;
            if (map.containsKey(outerCode)) {
              outer = map.get(outerCode);
            } else {
              outer = targetReader.getIndexElement(outerCode);
              map.put(outerCode, outer);
            }

            var instance = outer.getBase().getOverallMapping();
            if (patternOrigin.matches(instance)) {

              if (result.containsKey("#" + index.getCode())) {
                result.get("#" + index.getCode()).add(outer.getCode());
              } else {
                var list = new ArrayList<Integer>();
                list.add(outer.getCode());
                result.put("#" + index.getCode(), list);
              }

              map.remove(outerCode);
              var position = clusterListTarget.indexOf(outerCode);
              clusterListTarget.remove(position);

              position = clusterListOrigin.indexOf(indexCode);
              clusterListOrigin.remove(position);
              adicionado = true;
              break;
            }
          }
          if (!adicionado) {
            i++;
          }
        } catch (Throwable ex) {
          System.err.println("> falha na comparação dos elementos");
        }

        
        ElementFactory.setTempRepository(origin);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Map<String, List<Integer>> compatibility() {
    var result = new HashMap<String, List<Integer>>();
    this.process(result);
    return result;
  }

  public void toStore(Map<String, List<Integer>> data) {
    var base = Settings.get().folderOfMetadata() + "/" + this.origin + "/findings";

    var resource = new Resource(base, "#" + target + ".json");
    var result = new JsonObject();
    result.addProperty("origin", origin);
    result.addProperty("target", target);

    var clusters = new JsonObject();
    for (var entry : data.entrySet()) {
      var list = new JsonArray();
      for (var item : entry.getValue()) {
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