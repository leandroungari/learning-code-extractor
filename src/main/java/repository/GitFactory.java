package repository;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

public class GitFactory {

	private static Git git;
	
	public GitFactory() {
		
		if(git == null) git = Repository.get().git();
	}
	
	public List<Ref> branches() throws GitAPIException {
		
		return git
			.branchList()
			.setListMode(ListMode.REMOTE)
			.call();
	}
	
	public Ref branch(String name) throws IOException {
		
		return git
			.getRepository()
			.getRefDatabase()
			.findRef(name);
	}
	
}
