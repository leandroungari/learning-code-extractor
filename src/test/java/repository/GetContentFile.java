package repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.eclipse.jgit.lib.ObjectId;
import org.junit.Before;
import org.junit.Test;

import repository.exception.GitRepositoryException;

public class GetContentFile {

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
	public void listFiles() throws IOException {
		
		//7a58a45ba139f0b7e56bc2ffbf7733fa5351666c
		//
		System.out.println("\n=== List of files ===");
		repo
		.branch("refs/remotes/origin/master")
		.commit(ObjectId.fromString("7a58a45ba139f0b7e56bc2ffbf7733fa5351666c"))
		.allFiles()
		.forEach(file -> {
			System.out.println(file.name());
		});

	}
	
	@Test
	public void contentFile() throws IOException {
		
		//core/src/main/java/com/google/common/truth/AbstractArraySubject.java
		
		var file = repo
		.branch("refs/remotes/origin/master")
		.commit(ObjectId.fromString("7a58a45ba139f0b7e56bc2ffbf7733fa5351666c"))
		.filter("core/src/main/java/com/google/common/truth/AbstractArraySubject.java");
	
		System.out.println("\n=== ID ===");
		System.out.println(file.id().getName());
		System.out.println("\n=== Content of file ===");
		System.out.println(file.get());
	}
	
}
