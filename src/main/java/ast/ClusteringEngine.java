package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.clustering.Cluster;
import ast.extractor.CommitMappings;
import ast.extractor.elements.ElementFactory;
import ast.extractor.mapping.Mapping;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;
import repository.Repository;
import spoon.SpoonException;

public class ClusteringEngine {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  private Cluster cluster;

  public ClusteringEngine(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;

    this.cluster = new Cluster(repository, branch);
    ElementFactory.setTempRepository(repository);
  }

  public ClusteringEngine(String repository, String branch, Integer from, Integer to) {
    this.repository = repository;
    this.branch = branch;
    this.from = from;
    this.to = to;

    this.cluster = new Cluster(repository, branch);
    ElementFactory.setTempRepository(repository);
  }

  public void generate() {
    var counter = 0;
    for (var commit : getCommits()) {
      var items = getMappingsFromCommit(commit);
      if(items != null) {
        var mappings = items.get();
        System.err.println("> clustering commit #" + counter + " " + commit);
        identifyCluster(mappings, commit);
      } else {
        System.out.println("> clustering commit não encontrado: " + commit);
      }
      counter++;
    }
  }

  private Boolean isSameTypeMappings(Mapping base, Mapping other) {
    var srcType = base.source().type();
    var destType = base.destination().type();

    return (srcType.equals(other.source().type()) ||
            destType.equals(other.destination().type()));
  }

  private List<Mapping> filterMappings(List<Mapping> list) {
    var result = new ArrayList<Mapping>();
    for(var item: list) {
      if(item instanceof UpdateMapping || item instanceof MoveMapping) {
        result.add(item);
      }
    }
    return result;
  }

  private void identifyCluster(List<Mapping> items, String commit) {
    //filtrando mapeamentos
    var mappings = this.filterMappings(items);
    //ordenando mapeamentos
    mappings.sort((a, b) -> {
      var first = a.source().type();
      var sec = b.source().type();
      return first.compareTo(sec);
    });

    //extraindo clusters
    for(var mapping: mappings) {
      var added = false;
      var type = mapping.source().type();
      for(var item: cluster.getItems(type)) {
        if(isSameTypeMappings(item.getBase().getMapping(), mapping)) {
          try {
            if(item.getPattern().matches(mapping)) {
              item.add(mapping, commit);
              added = true;
              break;
            }
          } catch(SpoonException ex) {
            //System.err.println(ex.getMessage());
          }
        }
      }
      if(!added) {
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

  private List<String> getCommits() {
    var instance = createRepository();
    try {
      List<String> items = null;
      if(from != null && to != null) {
        items = instance.branch(branch)
          .commits(from, to)
          .stream()
          .map(item -> item.id().getName())
          .collect(Collectors.toList());
      } else {
        items = instance.branch(branch)
          .commits()
          .stream()
          .map(item -> item.id().getName())
          .collect(Collectors.toList());

        items.remove(0);
      }

      
      return items;
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    return null;
  }

    
}