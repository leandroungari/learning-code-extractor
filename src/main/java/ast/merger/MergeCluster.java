package ast.merger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.cluster.ClusterSet;
import ast.cluster.Item;
import ast.cluster.ItemPersistor;
import ast.extractor.elements.ElementFactory;
import ast.findings.ClusterItemReader;
import ast.snapshot.Snapshot;

public class MergeCluster {

  private String repository;
  private String branch;

  private ClusterSet set;

  private List<Integer> list;
  private ClusterItemReader reader;
  private ItemPersistor persistor;
  private HashMap<Integer,Item> map;

  public MergeCluster(String repository, String branch) throws GitAPIException {
    Snapshot.create(repository, branch);
    ElementFactory.setTempRepository(this.repository);


    this.repository = repository;
    this.branch = branch;
    this.set = new ClusterSet(this.repository, this.branch);
    this.persistor = new ItemPersistor(this.set);

    this.map = new HashMap<>();
  }

  public void merge() throws IOException {
    this.reader = new ClusterItemReader(this.set);
    this.list = reader.getListOfClusters();

    var i = 0;
    while (i < list.size()) {
      var cluster = this.getElement(i);
      if (cluster != null) {
        this.tryMergeWithThisCluster(cluster, i);
      }
      i++;
    }
  }

  private Item getElement(Integer id) {
    
    var code = list.get(id);
    
    var item = map.get(code);
    if(item != null) {
      return item;
    }

    try {
      item = reader.getItemElement(code);
      //if(item.getItems().size() > 1) {
        map.put(code, item);
      //}

      return item;
    } catch(Throwable ex) {
      return null;
    }
  }

  private void tryMergeWithThisCluster(Item cluster, Integer position) {
    var i = position + 1;
    while(i < list.size()) {
      var item = this.getElement(i);
      if(item != null &&
        cluster.getPattern().matches(item.getBase().getOverallMapping())) {
        this.joinClusters(cluster, item, i);
      } else {
        i++;
      }
    }
  }

  private void joinClusters(Item main, Item other, int otherPosition) {
    System.err.println("> Merge " + main.getCode() + " with " + other.getCode());

    main.getItems().addAll(other.getItems());
    persistor.write(main);

    persistor.delete(other);
    map.remove(other.getCode());

    list.remove(otherPosition);
  }
}