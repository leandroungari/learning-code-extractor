package repository;

import static org.junit.Assert.assertEquals;

import java.nio.file.FileAlreadyExistsException;

import org.junit.Before;
import org.junit.Test;

public class GetDiffFilesBetweenCommits {

	private Repository repo;
	
	@Before
	public void start() throws FileAlreadyExistsException {
		
		repo = new Repository(
			"https://github.com/danilofes/refactoring-toy-example.git",
			"/home/leandroungari/refactoring-toy-example"
		);
	}
	
	@Test
	public void run() {
		
		var list = repo
			.commit("d4bce13a443cf12da40a77c16c1e591f4f985b47")
			.diff();
		
		System.out.println(list);
		assertEquals(2, list.size());
	}
	
}
