package util;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class ListFoldersInAnotherFolder {

  private File parent;
  
  @Before
  public void before() {
    parent = new File("/home/leandroungari/Documents/data/metrics");
  }
  
  @Test
  public void quantity() {
    
    var num = FileUtil
      .listOfFolders(parent)
      .size();
    
    assertEquals(1, num);
  }
  
  @Test
  public void nameOfFirstFolder() {
    
    var folder = FileUtil
      .listOfFolders(parent)
      .get(0);
    
    assertEquals("refactoring-toy-example", folder);
       
  }
}
