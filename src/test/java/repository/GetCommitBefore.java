package repository;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.ObjectId;
import org.junit.Before;
import org.junit.Test;

import repository.exception.GitRepositoryException;

public class GetCommitBefore {

	private Repository repo;
	
	@Before
	public void before() throws GitRepositoryException {
		
		repo = Repository.create(
			new File("/home/leandroungari/Documents/repositories/truth")
		);
	}
	
	@Test
	public void mostRecentCommit() throws IOException {
		
		//current
		//7a58a45ba139f0b7e56bc2ffbf7733fa5351666c
		//before
		//cf94032329283f44d0d91ee986bdfb9111298b2b
		var before = repo
			.branch("refs/remotes/origin/master")
			.commit(ObjectId.fromString("7a58a45ba139f0b7e56bc2ffbf7733fa5351666c"))
			.before();
		
		assertEquals(
			"cf94032329283f44d0d91ee986bdfb9111298b2b",
			before.id().getName()
		);
	}
	
	@Test
	public void mostRecentCommitBefore() throws IOException {
		
		//current
		//7a58a45ba139f0b7e56bc2ffbf7733fa5351666c
		//before of before
		//30e3c4336541f826a105bbaffab28b04582ed1f9
		var before = repo
			.branch("refs/remotes/origin/master")
			.commit(ObjectId.fromString("7a58a45ba139f0b7e56bc2ffbf7733fa5351666c"))
			.before()
			.before();
		
		assertEquals(
			"30e3c4336541f826a105bbaffab28b04582ed1f9",
			before.id().getName()
		);
	}
}
