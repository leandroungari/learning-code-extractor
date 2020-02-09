package storage;

import java.io.IOException;

import org.junit.Test;

import core.controller.SettingsController;

public class StorageTest {

  private class Person {

    private String name;
    private Integer age;

    Person(String name, Integer age) {
      this.name = name;
      this.age = age;
    }

  }

  private static final String FILE = "teste.csv";
  private static String FOLDER = 
    SettingsController.get().folderOfMetadata().getAbsolutePath();

  @Test
  public void writeCSVFile() throws IOException {

    var storage = new Storage(FOLDER);
    
    storage.<Person>csv(FILE)
    .add(new Person("Leandro", 23))
    .add(new Person("Laura", 21))
    .add(new Person("Bruno", 24))
    .write();
  }
}