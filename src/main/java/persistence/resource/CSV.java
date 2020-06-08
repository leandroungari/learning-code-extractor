package persistence.resource;

import java.util.List;

import persistence.csv.CSVTransformer;

public class CSV<T extends CSVTransformer<T>> implements Storable<T> {

  private T target;

  public CSV(T target) {
    this.target = target;
  }

  @Override
  public String write() {
    var header = List.of(target.getHeader());
    var topLine = String.join(",", header);
    var items = target.toCSV();
    items.add(0, topLine);
    return String.join("\n", items);
  }

  @Override
  public T read(String content) {
    var items = content.split("\n");
    var list = List.of(items).subList(1, items.length-1);
    return target.fromCSV(list);
  }
}