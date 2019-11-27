package core;

public abstract class Feature<T> {

  protected Player player;

  protected Feature() {

    player = new Player();
  }
  
  public abstract Feature<T> setParams(String...params);
  public abstract T  action();
}