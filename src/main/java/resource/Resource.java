package resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Resource<T> {
  
  private File resource;
  private T item;

  public Resource(String folder, String file) {
    var parent = new File(folder);
    if(!parent.exists()) {
      parent.mkdirs();
    }
    this.resource = new File(folder + "/" + file);
  }

  public File toFile() {
    return this.resource;
  }

  public void setContent(T content) {
    this.item = content;
  }

  public void write(Storable<T> store) throws IOException {
    System.out.println(resource.getAbsolutePath());
    this.createFile(store.write(item));
  }

  public T read(Storable<T> store) throws IOException {
    return store.read(this.readFile());
  }

  public void createFile(String data) throws IOException {
    var out = new FileOutputStream(resource);
    out.write(data.getBytes());
    out.close();
	}
	
  public String readFile() throws IOException {
    var in = new FileInputStream(resource);
    var data = new String(in.readAllBytes());
    in.close();
    return data;
	}
}