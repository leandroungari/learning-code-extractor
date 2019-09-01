package repository;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.junit.Before;
import org.junit.Test;

public class GetCommitBefore {

	private Repository repo;
	
	@Before
	public void start() throws FileAlreadyExistsException {
		
		repo = new Repository(
			"https://github.com/danilofes/refactoring-toy-example.git",
			"/home/leandroungari/refactoring-toy-example"
		);
	}
	
	
	@Test
	public void intermediateCommit() 
		throws MissingObjectException, 
					  IncorrectObjectTypeException, 
					  IOException {

		var id = repo
			.commit("9a5c33b16d07d62651ea80552e8782974c96bb8a")
			.before()
			.id();
			
		System.out.println(id + " -- " + "a5a7f852e45c7cadc8d1524bd4d14a1e39785aa5");
		assertEquals(id, "a5a7f852e45c7cadc8d1524bd4d14a1e39785aa5");
		//a5a7f852e45c7cadc8d1524bd4d14a1e39785aa5
	}
	
	@Test
	public void mostRecentCommit() 
		throws MissingObjectException, 
					  IncorrectObjectTypeException, 
					  IOException {

		var id = repo
			.commit("d4bce13a443cf12da40a77c16c1e591f4f985b47")
			.before()
			.id();
		
		System.out.println(id + " -- " + "9a5c33b16d07d62651ea80552e8782974c96bb8a");
		assertEquals(id, "9a5c33b16d07d62651ea80552e8782974c96bb8a");
		//9a5c33b16d07d62651ea80552e8782974c96bb8a
	}
	
	@Test
	public void initialCommit() 
		throws MissingObjectException, 
					  IncorrectObjectTypeException, 
					  IOException {

		var id = repo
			.commit("819b202bfb09d4142dece04d4039f1708735019b")
			.before()
			.id();
		
		System.out.println(id + " -- " + null);
		assertEquals(id, null);
		//null
	}
}
