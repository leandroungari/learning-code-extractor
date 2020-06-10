package persistence.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Resource {
  
  private File resource;
  private Boolean isFolder;

  public Resource(String folder, String file) {
    var parent = new File(folder);
    if(!parent.exists()) {
      parent.mkdirs();
    }
    this.resource = new File(folder + "/" + file);
    this.isFolder = false;
  }

  public Resource(String folder) {
    this.resource = new File(folder);
    if(!resource.exists()) {
      resource.mkdirs();
    }
    this.isFolder = true;
  }

  public File toFile() {
    return this.resource;
  }

  /**
   * Escreve um novo arquivo do recurso somente
   * se o arquivo não existir
   * @param store Formato de armazenamento
   * @throws IOException
   */
  public void write(Storable<?> store) throws IOException {
    if(this.isFolder) 
      throw new Error("You cannot write a folder, it must be a file!");
    this.createFile(store.write(), false);
  }

  /**
   * Escreve um novo arquivo do recurso somente
   * se o arquivo não existir
   * @param store Formato de armazenamento
   * @param override Opção de forçar a reescrita de um arquivo existente
   * @throws IOException
   */
  public void write(Storable<?> store, Boolean override) throws IOException {
    if(this.isFolder) 
      throw new Error("You cannot write a folder, it must be a file!");
    this.createFile(store.write(), override);
  }

  public <T> T read(Storable<T> store) throws IOException {
    if(this.isFolder) 
      throw new Error("You cannot read a folder, it must be a file!");
    return store.read(this.readFile());
  }

  private void createFile(String data, boolean override) throws IOException {
    if(!resource.exists() || override) {
      var out = new FileOutputStream(resource);
      out.write(data.getBytes());
      out.close();
    }
	}
	
  private String readFile() throws IOException {
    if(resource.exists()) {
      var in = new FileInputStream(resource);
      var data = new String(in.readAllBytes());
      in.close();
      return data;
    }
    return null;
  }
  
  /**
   * Remove o arquivo ou pasta do recurso
   * referido
   */
  public void delete() {
    this.deleteRecursive(resource);
  }

  private void deleteRecursive(File element) {
    if (element.isDirectory()) {
      for (File sub : element.listFiles()) {
        deleteRecursive(sub);
      }
    }
    element.delete();
  }
}