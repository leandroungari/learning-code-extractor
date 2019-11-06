package core.result;

import org.junit.Test;

import core.result.tuple.Pair;


public class Tuple {
  
  @Test
  public void pair() {

    var pair = new Pair<String,Integer>("Leandro", 23);
    System.out.println(pair);
  }
}
