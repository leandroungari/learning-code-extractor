package repository.metrics;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.junit.Before;
import org.junit.Test;

import repository.Repository;
import repository.exception.GitRepositoryException;
import repository.metrics.CKMetric;

public class ObtainingMetrics {

	private Repository repo;
	
	@Before
	public void before() 
		throws FileAlreadyExistsException, 
						GitRepositoryException {
		
		repo = Repository.create(new File(
  		"/home/leandroungari/Documents/repositories/refactoring-toy-example"
  	));
	}
	
	@Test
	public void originalImplementation() {
		
		System.out.println("\n=== Original implementation ===");
		new CKMetric().calculate(
			"/home/leandroungari/Documents/repositories/refactoring-toy-example", 
			result -> {
				System.out.println(result.getFile().toString());
				System.out.println("WMC: " + result.getWmc());
			}
		);
	}
	
	@Test
	public void currentCommitInMaster() throws IOException {
		
		System.out.println("\n=== Current commit in master ===");
		var commitId = repo
			.branch("refs/remotes/origin/master")
			.commit("d4bce13a443cf12da40a77c16c1e591f4f985b47")
			.id();
		
		new CKMetric().calculate(commitId, result -> {
			System.out.println(result.getFile().toString());
			System.out.println("WMC: " + result.getWmc());
		});
	}
	
	@Test
	public void latest2ndCommitInMaster() throws IOException {
		System.out.println("\n=== 2nd commit in master ===");
		var commitId = repo
			.branch("refs/remotes/origin/master")
			.commit("d4bce13a443cf12da40a77c16c1e591f4f985b47")
			.before()
			.id();
		
		new CKMetric().calculate(commitId, result -> {
			System.out.println(result.getFile().toString());
			System.out.println("WMC: " + result.getWmc());
		});
	}
	
}
