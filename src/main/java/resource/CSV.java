package resource;

import java.util.List;

import core.csv.CSVTransformer;

public class CSV<T extends CSVTransformer<T>> implements Storable<T> {

  private T target;

  public CSV(T target) {
    this.target = target;
  }

  @Override
  public String write() {
    return String.join("\n", target.toCSV());
  }

  @Override
  public T read(String content) {
    return target.fromCSV(List.of(content.split("\n")));
  }
}