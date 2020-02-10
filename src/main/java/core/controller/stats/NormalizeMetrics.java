package core.controller.stats;

import java.util.HashMap;
import java.util.Map;

import core.data.MetricsData;

public class NormalizeMetrics {

  private Map<String,MetricsData> data;
  private Map<String,MetricsData> limits;

  public NormalizeMetrics(Map<String,MetricsData> metrics) {
    data = metrics;
  }

  public Map<String, MetricsData> execute() {
    
    limits = extractMinMaxOfRangeOfValues();
    return normalize();
  }

  public Map<String,MetricsData> normalize() {

    var result = new HashMap<String,MetricsData>();
    data.forEach((commit,values) -> {
      
      var metric = new MetricsData();
      metric.setCbo(transform(
        limits.get("min").getCbo(), 
        limits.get("max").getCbo(), 
        values.getCbo()
      ));
      metric.setWmc(transform(
        limits.get("min").getWmc(), 
        limits.get("max").getWmc(), 
        values.getWmc()
      ));
      metric.setNosi(transform(
        limits.get("min").getNosi(), 
        limits.get("max").getNosi(), 
        values.getNosi()
      ));
      metric.setRfc(transform(
        limits.get("min").getRfc(), 
        limits.get("max").getRfc(), 
        values.getRfc()
      ));
      metric.setDit(transform(
        limits.get("min").getDit(), 
        limits.get("max").getDit(), 
        values.getDit()
      ));

      
    });

    return result;
  }

  private Double transform(Double min, Double max, Double value) {

    return (value - min/(max - min != 0 ? max - min : 1));
  }


  private Map<String,MetricsData> extractMinMaxOfRangeOfValues() {

    return Map.of(
      "min", extractMin(),
      "max", extractMax()
    );
  }

  private MetricsData extractMin() {

    var result = new MetricsData();

    result.setWmc(
      data.values().stream()
      .map(metric -> metric.getWmc()).min(Double::compare).get()
    );

    result.setCbo(
      data.values().stream()
      .map(metric -> metric.getCbo()).min(Double::compare).get()
    );

    result.setDit(
      data.values().stream()
      .map(metric -> metric.getDit()).min(Double::compare).get()
    );

    result.setNosi(
      data.values().stream()
      .map(metric -> metric.getNosi()).min(Double::compare).get()
    );

    result.setRfc(
      data.values().stream()
      .map(metric -> metric.getNosi()).min(Double::compare).get()
    );

    return result;
  }

  private MetricsData extractMax() {

    var result = new MetricsData();

    result.setWmc(
      data.values().stream()
      .map(metric -> metric.getWmc()).max(Double::compare).get()
    );

    result.setCbo(
      data.values().stream()
      .map(metric -> metric.getCbo()).max(Double::compare).get()
    );

    result.setDit(
      data.values().stream()
      .map(metric -> metric.getDit()).max(Double::compare).get()
    );

    result.setNosi(
      data.values().stream()
      .map(metric -> metric.getNosi()).max(Double::compare).get()
    );

    result.setRfc(
      data.values().stream()
      .map(metric -> metric.getNosi()).max(Double::compare).get()
    );

    return result;
  }


}