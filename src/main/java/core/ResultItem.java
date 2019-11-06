package core;

import java.util.HashMap;
import java.util.Map;

public class ResultItem<T extends Comparable<T>> {

  private Map<String, T> data;
  
  private ResultItem() {
    data = new HashMap<String,T>();
  }

  public static <T extends Comparable<T>> ResultItem<T> create() {
    return new ResultItem<T>();
  }

  public ResultItem<T> add(String key, T value) {
    data.put(key, value);
    return this;
  }

  public T get(String key) {
    return data.get(key);
  }

  public Boolean aboveOf(String metric, T value) {
    return get(metric).compareTo(value) == 1;
  }

  public Boolean belowOf(String metric, T value) {
    return get(metric).compareTo(value) == -1;
  }

  public Boolean equalTo(String metric, T value) {
    return data.get(metric).compareTo(value) == 0;
  } 
}
