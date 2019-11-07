package core.result;

import org.junit.Test;

public class DataStructList {

  @Test
  public void convertListToArray() throws Exception {
    
    var list = new List<Integer>();
    list.add(12,2,5,7,1);

    list.forEach(item -> System.out.println("-- " + item));
    
    var teste = list.toArray();
    
    System.out.println(teste[0]);
  }
}
