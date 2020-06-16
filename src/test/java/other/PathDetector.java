package other;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

import ast.snapshot.Snapshot;

public class PathDetector {

  @Test
  public void searchingMethod() throws GitAPIException {

    var snapshot = new Snapshot(
      "junit4", 
      "50a285d3ce69b4556ac46d8633f6beb4527b4679"
    );

    var item = snapshot
      .commit("4b1869ebb8002e5d0b82ab55460f6126043c9ec4")
      .model("#subPackage[name=junit]#subPackage[name=swingui]#containedType[name=TestRunner]#method[signature=aboutToStart(junit.swingui.Test)]");

    System.out.println(item);
  }

  @Test
  public void verifyAssert() throws GitAPIException {
    //continuar testando
    var path = "#containedType[name=Assert]#method[signature=assertEquals(byte,byte)]";
    var snapshot = new Snapshot(
      "junit4", 
      "50a285d3ce69b4556ac46d8633f6beb4527b4679"
    );

    var item = snapshot
      .commit("4b1869ebb8002e5d0b82ab55460f6126043c9ec4")
      .model(path);

    System.out.println(item);
  }
}