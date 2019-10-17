package repository;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.ObjectId;
import org.junit.Before;
import org.junit.Test;

import repository.exception.GitRepositoryException;

public class GetAllFilesByCommit {

	private Repository repo;
	
	@Before
	public void before() throws GitRepositoryException {
		
		repo = Repository.create(
			new File("/home/leandroungari/Documents/repositories/truth")
		);
	}
	
	@Test
	public void allFilesAtCommit() 
		throws IOException {
		
		//refs/remotes/origin/master
		//7a58a45ba139f0b7e56bc2ffbf7733fa5351666c
		
		var number = repo
			.branch("refs/remotes/origin/master")
			.commit(ObjectId.fromString("7a58a45ba139f0b7e56bc2ffbf7733fa5351666c"))
			.allFiles()
			.size();
		
		System.out.println("Total of files: " + number);
		assertTrue(number > 0);
	}
	
	@Test
	public void allJavaFilesAtCommit() throws IOException {
		
		var number = repo
			.branch("refs/remotes/origin/master")
			.commit(ObjectId.fromString("7a58a45ba139f0b7e56bc2ffbf7733fa5351666c"))
			.allFilesByExtension(".java")
			.size();
		
		System.out.println("Total of Java files: " + number);
		assertTrue(number > 0);
	}
	
}
