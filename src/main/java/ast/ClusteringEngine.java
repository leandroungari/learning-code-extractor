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

public class ClusteringEngine {

  private String repository;
  private String branch;

  private Cluster cluster;

  public ClusteringEngine(String repository, String branch) {
    this.repository = repository;
    this.branch = branch;

    this.cluster = new Cluster(repository, branch);
  }

  public void generate() {
    for (var commit : getCommits()) {
      var mappings = getMappingsFromCommit(commit).get();
      System.out.println(commit);
      identifyCluster(mappings, commit);
    }
  }

  private void identifyCluster(List<Mapping> mappings, String commit) {
    for(var mapping: mappings) {
      
      if(mapping instanceof UpdateMapping || mapping instanceof MoveMapping) {
        var added = false;
        for(var item: cluster.getItems()) {
          if(item.getPattern().matches(mapping)) {
            item.add(mapping, commit);
            added = true;
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
      var items = instance.branch(branch)
        .commits()
        .stream()
        .map(item -> item.id().getName())
        .collect(Collectors.toList());

      items.remove(0);
      return items;
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
    return null;
  }

    
}