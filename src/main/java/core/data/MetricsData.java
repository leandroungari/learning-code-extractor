package core.data;

import java.util.HashMap;
import java.util.Map;

import com.github.mauricioaniche.ck.CKClassResult;

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
    
    files = new HashMap<String, Double[]>();
  }
  
  public void addFiles(
    String name,
    CKClassResult result
  ) {
    
    files.put(
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
    files.put(file, metrics);
  }
  
  public String[] getMetrics() {
    return metrics;
  }
  
  public Map<String, Double[]> getFiles() {
    return files;
  }
}
