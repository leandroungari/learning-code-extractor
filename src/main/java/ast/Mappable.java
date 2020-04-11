package ast;


import com.google.gson.JsonElement;

public interface Mappable {

  JsonElement toMap();
}