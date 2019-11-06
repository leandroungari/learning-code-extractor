package core.result;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class List<T> {

  private ArrayList<T> data;

  public List() {
    data = new ArrayList<>();
  }

  public static <A> List<A> of(A... list) {
    var result = new List<A>();
    result.add(list);
    return result;
  }

  public void add(T... items) {
    for(var i = 0; i < items.length; i++) {
      data.add(items[i]);
    }
  }

  public void add(T item) {
    
    data.add(item);
  }

  public Boolean contains(T item) {

    return data.contains(item);
  }

  public void remove(T item) {
    data.remove(item);
  }

  public void remove(Integer position) {
    data.remove(position);
  }

  public void forEach(Consumer<T> consumer) {
    
    for(var i = 0; i < data.size(); i++) {
      consumer.accept(data.get(i));
    }
  }

  public void forEach(BiConsumer<T,Integer> consumer) {
    
    for(var i = 0; i < data.size(); i++) {
      consumer.accept(data.get(i), i);
    }
  }

  public List<T> filter(Function<T,Boolean> action) {
    var result = new List<T>();
    for(var i = 0; i < data.size(); i++) {
      if(action.apply(data.get(i))) {
        result.add(data.get(i));
      }
    }
    return result;
  }

  public List<T> filter(BiFunction<T,Integer,Boolean> action) {
    var result = new List<T>();
    for(var i = 0; i < data.size(); i++) {
      if(action.apply(data.get(i), i)) {
        result.add(data.get(i));
      }
    }
    return result;
  }

  public <A> List<A> map(BiFunction<T,Integer,A> action) {
    var result = new List<A>();
    for(var i = 0; i < data.size(); i++) {
      result.add(action.apply(data.get(i), i));
    }
    return result;
  }

  public <A extends Comparable<A>> A reduce(BiFunction<A, T, A> action, A initial) {
    for(var i = 0; i < data.size(); i++) {
      initial = action.apply(initial, data.get(i));
    }
    return initial;
  }

  public Integer size() {
    return data.size();
  }

}
