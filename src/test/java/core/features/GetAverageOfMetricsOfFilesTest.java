package core.features;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import core.Player;

public class GetAverageOfMetricsOfFilesTest {

  private final String REPOSITORY = "refactoring-toy-example";
  private final String BRANCH = "d4bce13a443cf12da40a77c16c1e591f4f985b47";
  private final String FROM = "0";
  private final String TO = "64";
  private final String STEP = "4";

  private static Player player;

  @BeforeClass
  public static void start() {
    player = new Player();
  }

  @Test
  public void run() {

    var result = player
      .<Map<String,Map<String,Double>>>get(FeatureType.AVERAGE_OF_METRICS_OF_FILES)
      .setParams(REPOSITORY,BRANCH,FROM,TO,STEP)
      .action();

    assertTrue(result != null);
  }

}