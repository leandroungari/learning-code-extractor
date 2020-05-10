package resource;

import java.io.IOException;

public interface Storable<T> {
  String write(T item) throws IOException;
  T read(String content) throws IOException;
}