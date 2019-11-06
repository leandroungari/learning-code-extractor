package core.result;

import java.util.HashMap;

import core.result.tuple.Pair;

public class Map<K,V> {
  
  private HashMap<K,V> data;

  public Map() {
    data = new HashMap<K,V>();
  }

  public static <A,B> Map<A,B> of() {
    return new Map<A,B>();
  }

  public Map<K,V> add(K key, V value) {

    data.put(key, value);
    return this;
  }

  public void remove(K key) {

    data.remove(key);
  }

  public Boolean contains(K key) {
    
    return data.get(key) != null;
  }

  public V get(K key) {

    if(contains(key)) {
      return data.get(key);
    }
    else {
      return null;
    }
  }

  public List<V> values() {
    List<V> result = new List<>();
    data
    .values()
    .forEach(value -> result.add(value));
    return result;
  }

  public List<Pair<K,V>> entries() {
    var result = new List<Pair<K,V>>();
    data
      .forEach((key,value) -> {
        result.add(new Pair<K,V>(key,value));
      });

    return result;
  }

  public Integer size() {
    return data.size();
  }
}
