package storage;

public interface Storable<T> {

  String[] attributes();
  String[] write();
  T read(String[] values);
}