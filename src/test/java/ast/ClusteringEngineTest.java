package ast;

import org.junit.Test;

public class ClusteringEngineTest {
  
  private static final String repository = "refactoring-toy-example";
  private static final String branch = "d4bce13a443cf12da40a77c16c1e591f4f985b47";

  @Test
  public void generate() {

    //TODO
    /**
     * 36287f7c3b09eff78395267a3ac0d7da067863fd
      #subPackage[name=org]#subPackage[name=animals]#containedType[name=Chicken]#method[signature=layEgg()]
     */
    var engine = new ClusteringEngine(repository, branch);
    engine.generate();
  }
}