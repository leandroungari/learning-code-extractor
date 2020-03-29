package ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ast.change.Change;

public class Changes implements Iterable<Change<?>> {

  private List<Change<?>> changes;

  public Changes() {
    changes = new ArrayList<>();
  }

  public void add(Change<?> change) {

    changes.add(change);
  }

  public Change<?> first() {

    return changes.get(0);
  }

  public Change<?> last() {

    return changes.get(size() - 1);
  }

  public Change<?> get(Integer position) {

    return changes.get(position);
  }

  public Integer size() {
    return changes.size();
  }

  @Override
  public Iterator<Change<?>> iterator() {
    
    return changes.iterator();
  }
}