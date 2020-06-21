package ast;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.clustering.Cluster;
import ast.extractor.CommitMappings;

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
  }

  public ClusteringEngine(String repository, String branch, Integer from, Integer to) {
    this.repository = repository;
    this.branch = branch;
    this.from = from;
    this.to = to;

    this.cluster = new Cluster(repository, branch);
  }

  public void generate() {
    for (var commit : getCommits()) {
      var mappings = getMappingsFromCommit(commit).get();
      System.err.println("> clustering commit " + commit);
      identifyCluster(mappings, commit);
    }
  }

  private Boolean isSameTypeMappings(Mapping base, Mapping other) {
    var srcType = base.source().type();
    var destType = base.destination().type();

    return (srcType.equals(other.source().type()) ||
            destType.equals(other.destination().type()));
  }

  private void identifyCluster(List<Mapping> mappings, String commit) {
    for(var mapping: mappings) {
      
      if(mapping instanceof UpdateMapping || mapping instanceof MoveMapping) {
        var added = false;
        for(var item: cluster.getItems()) {
          if(isSameTypeMappings(item.getBase().getMapping(), mapping)) {
            try {
              if(item.getPattern().matches(mapping)) {
                item.add(mapping, commit);
                added = true;
              }
            } catch(SpoonException ex) {
              System.err.println(ex.getMessage());
              ex.printStackTrace();
            }
          }
        }
        if(!added) {
          cluster.addItem(mapping, commit);
        }
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