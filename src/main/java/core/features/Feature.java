package core.features;

import core.Player;

public interface Feature<T> {

  final Player player = new Player();
  
  public abstract Feature<T> setParams(String...params);
  public abstract T  action();
}