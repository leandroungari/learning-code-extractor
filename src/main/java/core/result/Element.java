package core.result;


public class Element<T> {

  private T data;
  
  public Element(T value) {

    if(value == null) {
      throw new NullPointerException("This value is null");
    }

    data = value;
  }

  public Element<T> set(T value) {
    
    if(value == null) {
      throw new NullPointerException("This value is null");
    }

    data = value;
    return this;
  }

  public T get() {
    return data;
  }

  @SuppressWarnings("unchecked")
  public <A extends Comparable<T>> Boolean aboveOf(A value) {
    
    return ((Comparable<A>) data).compareTo(value) == 1;
  }

  @SuppressWarnings("unchecked")
  public <A extends Comparable<T>> Boolean belowOf(A value) {
    
    return ((Comparable<A>) data).compareTo(value) == -1;
  }

  @SuppressWarnings("unchecked")
  public <A extends Comparable<T>> Boolean equalTo(A value) {
    return ((Comparable<A>) data).compareTo(value) == -1;
  }
}
