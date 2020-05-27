package ast;

import ast.clustering.ClusterPersistor;

public class ClusteringEngine {
  
  private ClusterPersistor persistor;

  public ClusteringEngine(String repositoryName) {
    this.persistor = new ClusterPersistor(repositoryName);
  }

  
}