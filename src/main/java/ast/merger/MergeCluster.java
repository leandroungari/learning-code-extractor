package ast.merger;

import java.io.IOException;
import java.util.ArrayList;
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
  private HashMap<String,List<Integer>> types;

  private Integer counterI;

  public MergeCluster(String repository, String branch) throws GitAPIException {
    Snapshot.create(repository, branch);
    ElementFactory.setTempRepository(this.repository);


    this.repository = repository;
    this.branch = branch;
    this.set = new ClusterSet(this.repository, this.branch);
    this.persistor = new ItemPersistor(this.set);

    this.map = new HashMap<>();
    this.types = new HashMap<>();
  }

  public void merge() throws IOException {
    this.reader = new ClusterItemReader(this.set);
    this.list = reader.getListOfClusters();

    this.counterI = 0;
    while (counterI < list.size()) {
      var cluster = this.getElement(counterI, null);
      System.err.println("> #" + counterI + " cluster " + cluster.getCode());
      if (cluster != null) {
        this.tryMergeWithThisCluster(cluster);
      }
      //remove da lista de mapeamentos quando já verificado
      this.map.remove(cluster.getCode());
      counterI++;
    }
  }

  private Item getElement(Integer id, String currentType) {
    
    var code = list.get(id);

    if(this.counterI > 1 &&
       currentType != null &&
       !this.types.get(currentType).contains(code)) {
      
      return null;
    }
    
    var item = map.get(code);
    if(item != null) {
      return item;
    }

    try {
      item = reader.getItemElement(code);
      var type = item.getBase().getType();
      
      if(this.counterI < 1) {

        if(!types.containsKey(type)) {
          types.put(type, new ArrayList<>());
        }

        if(!types.get(type).contains(item.getCode())) {
          types.get(type).add(item.getCode());
        }
      }

      if(item.getItems().size() > 1 && map.size() < 1000) {
        map.put(code, item);
      }

      return item;
    } catch(Throwable ex) {
      return null;
    }
  }

  private void tryMergeWithThisCluster(Item cluster) {
    var i = this.counterI + 1;
    while(i < list.size()) {
      var item = this.getElement(i, cluster.getBase().getType());
      if(item != null &&
        cluster.getBase().getType().equals(item.getBase().getType()) &&
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
    types.get(other.getBase().getType()).remove(other.getCode());
    list.remove(otherPosition);
  }
}