package core.result;

import java.util.ArrayList;

public class ResultList<T extends Comparable<T>> {

  private ArrayList<T> data;

  private ResultList() {
    data = new ArrayList<>();
  }

  public static <A extends Comparable<A>> ResultList<A> create() {
    return new ResultList<>();
  }

  public
}
