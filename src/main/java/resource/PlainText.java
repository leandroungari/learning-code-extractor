package resource;

import java.io.IOException;

public class PlainText implements Storable<String> {

  @Override
  public String write(String content) throws IOException {
    return content;
  }

  @Override
  public String read(String content) throws IOException {
    return content;
  }
}