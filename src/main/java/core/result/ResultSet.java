package core.result;

import java.util.HashMap;

public class ResultSet<T extends Comparable<T>> {
  
  private HashMap<String,T> data;

  private ResultSet() {
    data = new HashMap<String,T>();
  }

  public static <A extends Comparable<A>> ResultSet<A> create() {
    return new ResultSet<A>();
  }

  public void add(String key, T value) {

    data.put(key, value);
  }

  public void remove(String key) {

    data.remove(key);
  }

  public Boolean contains(String key) {
    
    return data.get(key) != null;
  }

  public ResultItem<T> get(String key) {

    if(contains(key)) {
      return data.get(key);
    }
    else {
      return null;
    }
  }
}
