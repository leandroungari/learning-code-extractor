package storage;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.junit.Test;

import core.controller.SettingsController;
import storage.storagetest.Person;

public class StorageTest {

  private static String FOLDER = SettingsController.get().folderOfMetadata().getAbsolutePath();

  @Test
  public void writeCSVFile() throws IOException {

    var storage = new Storage(FOLDER);
    var file = "teste.csv";

    storage
    .<Person>csv(file)
    .write(new Person[]{
      new Person("Leandro", 23),
      new Person("Laura", 21),
      new Person("Bruno", 24)
    });
  }

  @Test
  public void readCSVFile() throws IOException {

    var storage = new Storage(FOLDER);
    var file = "teste.csv";

    var result = storage.<Person>csv(file)
      .read(Person.class);

    for(var p: result) {
      System.out.println(p.getName() + " -- " + p.getAge());
    }

    assertTrue("The size is 3", result.length == 3);
  }

  //TODO
  //fazer testes para formato JSON

  @Test
  public void writePlainTextFile() throws IOException {

    var file = "teste.txt";

    var storage = new Storage(FOLDER);
    
    System.out.println(
      storage
      .plainText(file)
      .read()
    );
  }

  @Test
  public void readPlainTextFile() throws IOException {

    var file = "teste.txt";

    var storage = new Storage(FOLDER);
  
    storage
    .plainText(file)
    .write("meu teste geral!");
  }


}