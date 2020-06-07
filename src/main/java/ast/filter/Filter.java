package ast.filter;

import java.util.List;

public abstract class Filter<T> {
  
  public abstract List<T> filter();
}