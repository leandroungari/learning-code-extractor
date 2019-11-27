package core;

public interface Feature<T> {

  Feature<T> setParams(String...params);
  T  action();
}