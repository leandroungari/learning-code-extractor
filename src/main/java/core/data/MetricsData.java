package core.data;

import java.util.HashMap;

import com.github.mauricioaniche.ck.CKClassResult;

public class MetricsData {

  private String[] metrics;
  private HashMap<String, Integer[]> files;
  
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
    
    files = new HashMap<String, Integer[]>();
  }
  
  public void addFiles(
    String name,
    CKClassResult result
  ) {
    
    files.put(
      name,
      new Integer[] {
        result.getCbo(),
        result.getDit(),
        result.getNumberOfFields(),
        result.getNumberOfMethods(),
        result.getNosi(),
        result.getRfc(),
        result.getWmc(),
        result.getLoc(),
        result.getLcom()
      }
    );
  }
  
  public String[] getMetrics() {
    return metrics;
  }
  
  public HashMap<String, Integer[]> getFiles() {
    return files;
  }
}
