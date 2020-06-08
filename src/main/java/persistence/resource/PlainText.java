package persistence.resource;

public class PlainText implements Storable<String> {

  private String target;

  public PlainText(String item) {
    this.target = item;
  }

  @Override
  public String read(String content) {
    return content;
  }

  @Override
  public String write() {
    return target;
  }
}