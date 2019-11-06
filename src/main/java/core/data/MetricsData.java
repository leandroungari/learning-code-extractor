package core.data;

import com.github.mauricioaniche.ck.CKClassResult;

import core.result.Map;

public class MetricsData {

  private String[] metrics;
  private Map<String, Double[]> files;
  
  public MetricsData() {
    
    metrics = new String[] {
      "CBO",
      "DIT",
      "Number of fields",
      "Number of methods",
      "NOSI",
      "RFC",
      "WMC",
      "LOC",
      "LCOM"
    };
    
    files = new Map<String, Double[]>();
  }
  
  public void addFiles(
    String name,
    CKClassResult result
  ) {
    
    files.add(
      name,
      new Double[] {
        Double.valueOf(result.getCbo()),
        Double.valueOf(result.getDit()),
        Double.valueOf(result.getNumberOfFields()),
        Double.valueOf(result.getNumberOfMethods()),
        Double.valueOf(result.getNosi()),
        Double.valueOf(result.getRfc()),
        Double.valueOf(result.getWmc()),
        Double.valueOf(result.getLoc()),
        Double.valueOf(result.getLcom())
      }
    );
  }

  public void addFile(String file, Double[] metrics) {
    files.add(file, metrics);
  }
  
  public String[] getMetrics() {
    return metrics;
  }
  
  public Map<String, Double[]> getFiles() {
    return files;
  }
}
