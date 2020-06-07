package core.csv;

import java.util.List;

public interface CSVTransformer<T> {
  
  List<String> toCSV();
  String[] getHeader();
  T fromCSV(List<String> content);
  String toString();
}