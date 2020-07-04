package ast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.clustering.Cluster;
import ast.extractor.CommitMappings;
import ast.extractor.cluster.Item;
import ast.extractor.elements.ElementFactory;
import ast.extractor.mapping.Mapping;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;
import ast.snapshot.Snapshot;
import persistence.Settings;
import persistence.resource.JSON;
import persistence.resource.PlainText;
import persistence.resource.Resource;
import repository.Repository;
import spoon.SpoonException;

public class ClusterExtractor {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  private Cluster cluster;

  private Integer counter;
  private Snapshot snapshot;
  private String base;

  public ClusterExtractor(String repository, String branch) throws GitAPIException {
    this.repository = repository;
    this.branch = branch;

    this.snapshot = Snapshot.create(this.repository, this.branch);
    ElementFactory.setTempRepository(this.repository);
    
    this.cluster = new Cluster(repository, branch);
    this.counter = 0;
    this.base = Settings.get().folderOfMetadata().getAbsolutePath();
  }

  public void execute() {
    var manager = loadBaseFile();
    for (var entry : manager.entrySet()) {
      var commit = entry.getKey();
      System.err.println("> clustering commit #" + counter + " " + commit);
      counter++;
      for (var item : entry.getValue().entrySet()) {
        var file = item.getKey();
        var mappings = getMappings(commit, file);
        if (mappings != null) {

          identifyCluster(mappings, commit);
        }
      }
      counter++;
    }
  }

  private List<Mapping> getMappings(String commit, String file) {
    var resource = new Resource(this.base + "/" + repository + "/multipattern/" + commit, file + ".json");
    try {
      var instance = new Item();
      instance.setSnapshot(snapshot);
      var item = resource.read(new JSON<>(instance));
      var list = item.getMappings();
      return list;
    } catch (Exception e) {
      // TODO Auto-generated catch block
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
        var contemDiferente = false;
        for (var map : entry.getValue().getAsJsonObject().entrySet()) {
          var valor = map.getValue().getAsInt();
          if (valor != 0) {
            contemDiferente = true;
            commit.put(map.getKey(), valor);
          }
        }

        if (contemDiferente) {
          result.put(entry.getKey(), commit);
        }

      });

      return result;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /*
   * public void generate() { for (var commit : getCommits()) { var items =
   * getMappingsFromCommit(commit); if(items != null) { var mappings =
   * items.get(); System.err.println("> clustering commit #" + counter + " " +
   * commit); identifyCluster(mappings, commit); } else {
   * System.out.println("> clustering commit não encontrado: " + commit); }
   * counter++; } }
   */

  private Boolean isSameTypeMappings(Mapping base, Mapping other) {
    var srcType = base.source().type();
    var destType = base.destination().type();

    return (srcType.equals(other.source().type()) || destType.equals(other.destination().type()));
  }

  private List<Mapping> filterMappings(List<Mapping> list) {
    var result = new ArrayList<Mapping>();
    for (var item : list) {
      if (item instanceof UpdateMapping || item instanceof MoveMapping) {
        result.add(item);
      }
    }
    return result;
  }

  private void identifyCluster(List<Mapping> items, String commit) {
    // filtrando mapeamentos
    var mappings = this.filterMappings(items);
    // ordenando mapeamentos
    mappings.sort((a, b) -> {
      var first = a.source().type();
      var sec = b.source().type();
      return first.compareTo(sec);
    });

    // extraindo clusters
    for (var mapping : mappings) {
      var added = false;
      var type = mapping.source().type();
      var elements = cluster.getItems(type);
      for (var item : elements) {
        if (isSameTypeMappings(item.getBase().getMapping(), mapping)) {
          try {
            if (item.getPattern().matches(mapping)) {
              item.add(mapping, commit);
              added = true;
              break;
            }
          } catch (SpoonException ex) {
            // System.err.println(ex.getMessage());
          }
        }
      }
      if (!added) {
        cluster.addItem(mapping, commit);
      }
    }
  }

  private CommitMappings getMappingsFromCommit(String id) {
    var reader = new MappingsReader(repository, branch);
    return reader.commit(id);
  }

  private Repository createRepository() {
    return Repository.create(repository);
  }

  /*
   * private List<String> getCommits() { var instance = createRepository(); try {
   * List<String> items = null; if(from != null && to != null) { items =
   * instance.branch(branch) .commits(from, to) .stream() .map(item ->
   * item.id().getName()) .collect(Collectors.toList()); } else { items =
   * instance.branch(branch) .commits() .stream() .map(item ->
   * item.id().getName()) .collect(Collectors.toList());
   * 
   * items.remove(0); }
   * 
   * 
   * return items; } catch (GitAPIException e) { e.printStackTrace(); } return
   * null; }
   */

}