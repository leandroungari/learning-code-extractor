package storage;

import java.io.File;
import java.io.IOException;
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
  private List<T> content;
  private Class<T> type;

  CSV(Storage store) {
    this.store = store;
    this.content = new ArrayList<>();
    this.type = null;
  }

  @SuppressWarnings("unchecked")
  public CSV<T> add(T item) {

    if(type == null) type = (Class<T>) item.getClass();
    content.add(item);
    return this;
  }

  public void write() throws IOException {

    var file = store.resource();
    var printer = createPrinter(file);

    var result = new ArrayList<List<String>>();
    for (var i = 0; i < content.size(); i++) {
      result.add(List.of(content.get(i).write()));
    }

    printer.printRecords(result);
    printer.flush();
    printer.close();
  }

  private CSVPrinter createPrinter(File file) 
    throws IOException {

    var writer = Files
      .newBufferedWriter(Paths.get(file.getAbsolutePath()));

    return new CSVPrinter(
      writer, 
      CSVFormat.DEFAULT
      .withHeader(content.get(0).attributes())
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
  public List<T> read(Class<T> type) 
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

    return result;
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