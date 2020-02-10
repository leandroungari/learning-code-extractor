package storage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class CSV<T extends Storable<T>> {

  private Storage store;

  CSV(Storage store) {
    this.store = store;
  }

  @SuppressWarnings("unchecked")
  public void write(T[] content) throws IOException {

    var type = (Class<T>) content.getClass().getComponentType();

    var headers = generateElement(type).attributes();
    var file = store.resource();
    var printer = createPrinter(file, headers);

    var result = new ArrayList<List<String>>();
    for (var i = 0; i < content.length; i++) {
      result.add(List.of(content[i].write()));
    }

    printer.printRecords(result);
    printer.flush();
    printer.close();
  }

  private CSVPrinter createPrinter(File file, String[] headers) 
    throws IOException {

    var writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()));

    return new CSVPrinter(
      writer, 
      CSVFormat.DEFAULT
      .withHeader(headers)
      .withDelimiter(',')
    );
  }

  /**
   * This method requires a empty constructor of type class
   * 
   * @param type The type of element in CSV file
   * @return
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public T[] read(Class<T> type) 
    throws IOException {

    var result = new ArrayList<T>();
    
    var file = store.resource();
    var parser = createParser(file);
    for (var record : parser) {
      var element = generateElement(type);
      var values = createValues(record, element);
      result.add(element.read(values));
    }

    parser.close();

    return result.toArray((T[]) Array.newInstance(type, result.size()));
  }

  private String[] createValues(CSVRecord record, T data) {
    var item = record.toMap();
    var e = new String[data.attributes().length];
    var i = 0;
    for(var attr: data.attributes()) {
      e[i++] = item.get(attr);
    }

    return e;
  }

  private CSVParser createParser(File file) 
    throws IOException {

    var reader = Files
      .newBufferedReader(Paths.get(file.getAbsolutePath()));
    
    return new CSVParser(reader,
      CSVFormat.DEFAULT
      .withHeader()
      .withIgnoreHeaderCase()
      .withTrim()
    );
  }

  private T generateElement(Class<T> type) {

    try {
    
      return type.getConstructor(new Class<?>[]{}).newInstance();

    } catch (NoSuchMethodException | 
      SecurityException | 
      InstantiationException | 
      IllegalAccessException | 
      IllegalArgumentException | 
      InvocationTargetException e1) {
      
      throw new Error("Ilegal constructor access and object creation");
    }
  }

}