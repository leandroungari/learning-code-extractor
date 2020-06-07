package core.csv;

public interface CSVTransformerItem<T> {
  
  T fromCSVItem(String[] content);
  String[] toCSVItem();
}