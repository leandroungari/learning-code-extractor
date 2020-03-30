package ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Changes implements Iterable<ChangeItem> {

  private List<ChangeItem> changes;

  public Changes() {
    changes = new ArrayList<>();
  }

  public void add(ChangeItem change) {

    changes.add(change);
  }

  public ChangeItem first() {

    return changes.get(0);
  }

  public ChangeItem last() {

    return changes.get(size() - 1);
  }

  public ChangeItem get(Integer position) {

    return changes.get(position);
  }

  public Integer size() {
    return changes.size();
  }

  @Override
  public Iterator<ChangeItem> iterator() {
    
    return changes.iterator();
  }
}