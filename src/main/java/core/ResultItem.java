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

  public Map<String, Double> get() {
    return data;
  }

  public Boolean aboveOf(String metric, Double value) {
    return data.get(metric) > value;
  }

  public Boolean belowOf(String metric, Double value) {
    return data.get(metric) < value;
  }

  public Boolean equalTo(String metric, Double value) {
    return data.get(metric) == value;
  } 
}
