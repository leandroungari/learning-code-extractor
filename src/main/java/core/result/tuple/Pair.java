package core.result.tuple;

public class Pair<A,B> {

  private A a;
  private B b;

  public Pair(A a, B b) {
    this.a = a;
    this.b = b;
  }

  public A one() {
    return a;
  }

  public B two() {
    return b;
  }
  
  @Override
  public String toString() {
    
    return new StringBuilder()
      .append("(")
      .append(a.toString())
      .append(", ")
      .append(b.toString())
      .append(")")
      .toString();
  }
}
