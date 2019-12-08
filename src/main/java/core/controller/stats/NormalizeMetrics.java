package core.controller.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class NormalizeMetrics {

  private Map<String,Map<String,Double>> data;
  private Map<String,Map<String,Double>> limits;

  NormalizeMetrics(Map<String,Map<String,Double>> metrics) {
    data = metrics;
  }

  public Map<String, Map<String, Double>> execute() {
    
    limits = extractMinMaxOfRangeOfValues();
    return normalize();
  }

  public Map<String,Map<String, Double>> normalize() {

    var result = new HashMap<String,Map<String,Double>>();
    data.forEach((commit,metrics) -> {
      result.put(commit, Map.<String,Double>of(
        "cbo", normalizeMetric("cbo", metrics),
        "dit", normalizeMetric("dit", metrics),
        "nosi", normalizeMetric("nosi", metrics),
        "rfc", normalizeMetric("rfc", metrics),
        "wmc", normalizeMetric("wmc", metrics)
      ));
    });

    return result;
  }

  private Double normalizeMetric(String metric, Map<String, Double> item) {

    return (
      item.get(metric) - limits.get(metric).get("min")/
      (
        limits.get(metric).get("max") - limits.get(metric).get("min") != 0 ?
        limits.get(metric).get("max") - limits.get(metric).get("min") :
        1
      )
    );
  }

  private Map<String,Map<String,Double>> initializeComparator() {
    
    var first = extractFirstElement();
    return cloneElement(first);
  }

  private Map<String, Map<String, Double>> cloneElement(Map<String, Double> element) {
    var result = new HashMap<String,Map<String,Double>>();
    element.forEach((metric,value) -> {
      result.put(metric, Map.of("min",value,"max",value));
    });
    return result;
  }


  private Map<String, Double> extractFirstElement() {

    return data
      .values()
      .stream()
      .collect(Collectors.toList())
      .get(0);
  }

  private Map<String,Map<String,Double>> extractMinMaxOfRangeOfValues() {

    var comparator = initializeComparator();
    var values = extractValuesFromMetrics(data);

    return generateMinMax(comparator, values);
  }

  private ArrayList<ArrayList<Double>> extractValuesFromMetrics(Map<String, Map<String, Double>> set) {

    var values = new ArrayList<ArrayList<Double>>();
    values.add(new ArrayList<Double>());
    values.add(new ArrayList<Double>());
    values.add(new ArrayList<Double>());
    values.add(new ArrayList<Double>());
    values.add(new ArrayList<Double>());
  
    for(Map<String,Double> item: set.values()) {
      
      values.get(0).add(item.get("cbo"));
      values.get(1).add(item.get("dit"));
      values.get(2).add(item.get("nosi"));
      values.get(3).add(item.get("rfc"));
      values.get(4).add(item.get("nosi"));
    }

    return values;
  }

  private Map<String, Map<String, Double>> generateMinMax(
    Map<String,Map<String,Double>> comparator,
    ArrayList<ArrayList<Double>> values) {

    var label = new String[]{"cbo","dit","nosi","rfc","wmc"};
    var pointer = 0;
    for(List<Double> list: values) {
      comparator.put(label[pointer], Map.<String,Double>of(
        "min", list.stream().min(Double::compare).get(),
        "max", list.stream().max(Double::compare).get()
      ));

      pointer++;
    }
    return comparator;
  }
}