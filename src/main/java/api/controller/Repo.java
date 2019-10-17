package api.controller;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import core.Player;
import core.data.Branch;
import core.data.Commit;
import core.data.CommitInfo;
import core.data.DiffFile;
import repository.exception.GitRepositoryException;

@RestController
public class Repo {

	private static Player player = new Player();

	@GetMapping("/repo/{repositoryName}/branches")
	@ResponseBody
	public Branch[] allBranches(
		@PathVariable String repositoryName)
		throws GitRepositoryException,
						GitAPIException {

		return player.branches(repositoryName);
	}

	@GetMapping("/repo/{repository}/{branch}/commits")
	@ResponseBody
	public Commit[] allCommits(
		@PathVariable String repository,
		@PathVariable String branch)
		throws GitRepositoryException,
						GitAPIException, 
						IOException {

		return player.commits(repository, branch);
	}

	@GetMapping("/repo/{repository}/{branch}/{commit}/info")
	@ResponseBody
	public CommitInfo commitInfo(
		@PathVariable String repository,
		@PathVariable String branch,
		@PathVariable String commit)
		throws GitRepositoryException,
		GitAPIException, IOException {

		return player.commitInfo(
			repository, 
			branch, 
			commit);
	}

	
	@GetMapping("/repo/{repository}/{branch}/{commit}/diffFiles")
	@ResponseBody
	public DiffFile[] diffFiles(
		@PathVariable String repository,
		@PathVariable String branch,
		@PathVariable String commit)
		throws GitRepositoryException,
		GitAPIException, IOException {

		return player.diffFilesFromCommit(
			repository, 
			branch, 
			commit);
	}
}
