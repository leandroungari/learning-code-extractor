package storage;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.junit.Test;

import core.controller.SettingsController;
import storage.storagetest.Person;

public class StorageTest {

  private static final String FILE = "teste.csv";
  private static String FOLDER = SettingsController.get().folderOfMetadata().getAbsolutePath();

  @Test
  public void writeCSVFile() throws IOException {

    var storage = new Storage(FOLDER);

    storage
    .<Person>csv(FILE)
    .add(new Person("Leandro", 23))
    .add(new Person("Laura", 21))
    .add(new Person("Bruno", 24))
    .write();
  }

  @Test
  public void readCSVFile() throws IOException {

    var storage = new Storage(FOLDER);
    
    var result = storage.<Person>csv(FILE)
      .read(Person.class);

    for(var p: result) {
      System.out.println(p.getName() + " -- " + p.getAge());
    }

    assertTrue("The size is 3", result.length == 3);
  }
}