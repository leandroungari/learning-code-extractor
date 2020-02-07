package storage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSV<T> {

  private Storage store;
  private List<T> content;

  CSV(Storage store) {
    this.store = store;
    this.content = new ArrayList<>();
  }

  public CSV<T> add(T item) {

    content.add(item);
    return this;
  }

  private String[] getAttributesOfElements() {
    
    if(content.isEmpty()) throw new Error("CSV content must not be empty!");
    
    var fields = content.get(0).getClass().getDeclaredFields();
    
    var names = new ArrayList<>();
    Arrays.asList(fields)
    .stream()
    .forEach(field -> {
      var name = field.getName();
      if(!(name.contains("this"))) {
        names.add(field.getName());
      }
    });

    return names.toArray(new String[fields.length]);
  }

  public void write() throws IOException {

    var file = store.resource();
    var writer = Files.newBufferedWriter(
      Paths.get(file.getAbsolutePath())
    );

    try(var printer = new CSVPrinter(
      writer, 
      CSVFormat.DEFAULT
      .withHeader(getAttributesOfElements())
    )) {

      var values = new ArrayList<String>();
      for(var item: content) {
        
        for(var attribute: getAttributesOfElements()) {
          if(attribute != null) {
            var field = item.getClass().getDeclaredField(attribute);
            field.setAccessible(true);
            values.add(field.get(item).toString());
          }
        }
        printer.printRecords(item);

        values.clear();
      }
      printer.close();
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }

    writer.close();
  }

  

}