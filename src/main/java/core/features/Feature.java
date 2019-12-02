package core.features;

import core.Player;
import core.controller.DataController;

public interface Feature<T> {

  final Player player = new Player();
  final DataController data = new DataController();
  
  public abstract Feature<T> setParams(String...params);
  public abstract T  action();
}