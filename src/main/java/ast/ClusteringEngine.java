package ast;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.clustering.Cluster;
import ast.extractor.CommitMappings;
import ast.extractor.mapping.Mapping;
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
      identifyCluster(mappings, commit);
    }
  }

  private void identifyCluster(List<Mapping> mappings, String commit) {
    for(var mapping: mappings) {
      var added = false;
      for(var item: cluster.getItems()) {
        if(item.getPattern().matches(mapping)) {
          item.add(mapping, commit);
        }
      }
      if(!added) {
        cluster.addItem(mapping);
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