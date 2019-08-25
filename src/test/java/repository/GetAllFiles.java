package repository;

import static org.junit.Assert.assertTrue;

import java.nio.file.FileAlreadyExistsException;

import org.junit.Test;

public class GetAllFiles {

	@Test
	public void run() throws FileAlreadyExistsException {
		
		//d4bce13a443cf12da40a77c16c1e591f4f985b47
		
		var repo = new Repository(
			"https://github.com/danilofes/refactoring-toy-example.git",
			"/home/leandroungari/refactoring-toy-example"
		);
		
		var list = repo
			.commit("d4bce13a443cf12da40a77c16c1e591f4f985b47")
			.allFiles();
		System.out.println(list);
		
		var listByExt = repo
			.commit("d4bce13a443cf12da40a77c16c1e591f4f985b47")
			.allFilesByExtension(".java");
		System.out.println(listByExt);
		
		
		assertTrue(list.size() > 0);
		
	}
	
}
