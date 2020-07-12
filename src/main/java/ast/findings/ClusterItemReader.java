package ast.findings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ast.cluster.ClusterSet;
import ast.cluster.Index;
import ast.cluster.Item;
import ast.cluster.ItemPersistor;
import persistence.Settings;

public class ClusterItemReader {

  private String repository;
  private ClusterSet clusterSet;
  private ItemPersistor persistor;

  public ClusterItemReader(ClusterSet clusterSet) {
    this.clusterSet = clusterSet;
    this.repository = clusterSet.getRepository();
    this.persistor = new ItemPersistor(this.clusterSet);
  }

  public List<Index> get() throws IOException {

    return getListOfClusters().stream().map(code -> {
      return getIndexElement(code);
    })
    .collect(Collectors.toList());
  }

  public List<Integer> getListOfClusters() throws IOException {
    var path = Settings.get().folderOfMetadata() + "/" + repository + "/clusters";
    try (Stream<Path> paths = Files.walk(Paths.get(path))) {
      return paths.filter(item -> {
        return !item.getFileName().toString().contains(repository) 
          && item.getFileName().toString().contains("#");
      }).map(item -> {
        return Integer.valueOf(
          item.getFileName().toString()
            .replace("#", "")
            .replace(".json", "")
        );
      })
      .collect(Collectors.toList());
    }
  }

  public Index getIndexElement(Integer code) {
    var cluster = new Item(code, this.clusterSet);
    cluster = persistor.read(cluster);

    return new Index(
      cluster.getCode(), 
      cluster.getBase(), 
      this.clusterSet
    );
  }
}