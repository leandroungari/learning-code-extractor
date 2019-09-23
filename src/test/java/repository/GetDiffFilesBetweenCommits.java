package repository;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.junit.Before;
import org.junit.Test;

import repository.exception.GitRepositoryException;

public class GetDiffFilesBetweenCommits {

	private Repository repo;
	
	@Before
	public void before() 
		throws FileAlreadyExistsException, 
						GitRepositoryException {
		
		repo = Repository.create(
			"https://github.com/google/truth.git",
  		new File("/home/leandroungari/Documents/repositories/truth")
		);
	}
	
	@Test
	public void diff() throws IOException {
		
		var diff = repo
		.branch("refs/remotes/origin/master")
		.commit("7a58a45ba139f0b7e56bc2ffbf7733fa5351666c")
		.diff();
		
		assertEquals(2, diff.size());
		
		diff.forEach(d -> {
			System.out.println(d.type());
			System.out.println(d.toString());
		});
	}
	
}
