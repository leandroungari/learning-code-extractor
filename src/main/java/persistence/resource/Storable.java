package persistence.resource;

public interface Storable<T> {
  String write();
  T read(String content);
}