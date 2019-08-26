package repository;

import java.nio.file.FileAlreadyExistsException;

import org.junit.Test;

public class GetContentFile {

	@Test
	public void run() throws FileAlreadyExistsException {
		
		var repo = new Repository(
			"https://github.com/danilofes/refactoring-toy-example.git",
			"/home/leandroungari/refactoring-toy-example"
		);
		
		
		//src/org/DogManager.java
		//d4bce13a443cf12da40a77c16c1e591f4f985b47
		var source = repo
			.commit("d4bce13a443cf12da40a77c16c1e591f4f985b47")
			.file("src/org/DogManager.java")	
			.get();
		
		System.out.println(source);
	}
}
