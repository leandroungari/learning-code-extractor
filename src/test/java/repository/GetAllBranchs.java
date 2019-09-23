package repository;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Before;
import org.junit.Test;

import repository.exception.GitRepositoryException;

public class GetAllBranchs {

	private Repository repo;
	
	@Before
	public void before() throws FileAlreadyExistsException, GitRepositoryException {
		
		repo = Repository.create(
			"https://github.com/google/truth.git",
  		new File("/home/leandroungari/Documents/repositories/truth")
		);
	}
	
	@Test
	public void listBranches() throws GitAPIException {
		
		assertEquals(5, repo.branches().size());
	}
	
	@Test
	public void getBranch() throws GitAPIException, IOException {
		
		for(Branch b: repo.branches()) {
			
			System.out.println(
					b.id().toString() + " -- " + 
					repo.branch(b.name()).id().toString()
			);
			
			assertEquals(
				b.id().toString(), 
				repo.branch(b.name()).id().toString()
			);
		}
	}
}
