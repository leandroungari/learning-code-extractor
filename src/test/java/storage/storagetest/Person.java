package storage.storagetest;

import storage.Storable;

public class Person implements Storable<Person> {

  private String name;
  private Integer age;

  public Person() {

  }

  public Person(String name, Integer age) {
    this.name = name;
    this.age = age;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public Integer getAge() {
    return age;
  }

  @Override
  public String[] write() {

    return new String[] { name, age.toString() };
  }

  @Override
  public Person read(String[] values) {
    setName(values[0]);
    setAge(Integer.valueOf(values[1]));
    return this;
  }

  @Override
  public String[] attributes() {
    return new String[] { "name", "age" };
  }

}