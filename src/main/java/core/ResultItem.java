package core;

import java.util.Map;

public class ResultItem {

  private Map<String, Double> data;
  
  private ResultItem() {
    data = null;
  }


  private void data(Map<String, Double> data) {
    this.data = data;
  }

  public static ResultItem create(Map<String, Double> item) {
    var result = new ResultItem();
    result.data(item);
    return result;
  }
}
