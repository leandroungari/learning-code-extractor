package metrics;

import org.junit.Test;

public class QualityMetricsExtractorTest {
  
  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void generate() {

    var extractor = new QualityMetricsExtractor(repository, branch);
    extractor.extract();
  }

  @Test
  public void generateFrom10To20() {

    var extractor = new QualityMetricsExtractor(repository, branch, 10, 20);
    extractor.extract();
  }
}