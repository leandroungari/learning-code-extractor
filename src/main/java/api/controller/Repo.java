package api.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import core.Player;
import core.data.Branch;
import core.data.Commit;
import core.data.DiffFile;
import core.features.FeatureType;
import repository.commit.CommitInfo;
import repository.exception.GitRepositoryException;

@RestController
public class Repo {

	private static Player player = new Player();

	@GetMapping("/repo/{name}/branches")
	@ResponseBody
	public Branch[] allBranches(
		@PathVariable String name)
		throws GitRepositoryException,
						GitAPIException {

		return player
			.repository(name)
			.branches();
	}

	@GetMapping("/repo/{name}/{branch}/commits")
	@ResponseBody
	public Commit[] allCommits(
		@PathVariable String name,
		@PathVariable String branch)
		throws GitRepositoryException,
						GitAPIException, 
						IOException {

		return player
			.repository(name)
			.commits(branch);
	}

	@GetMapping("/repo/{name}/{branch}/{commit}/info")
	@ResponseBody
	public CommitInfo commitInfo(
		@PathVariable String name,
		@PathVariable String branch,
		@PathVariable String commit)
		throws GitRepositoryException,
					 GitAPIException, 
					 IOException {

		return player
			.<CommitInfo>get(FeatureType.GET_COMMIT_INFO)
			.setParams(name, branch, commit)
			.action();
		

		/*return player
			.repository(name)
			.commitInfo(
				branch, 
				commit
			);*/
	}

	
	@GetMapping("/repo/{name}/{branch}/{commit}/diffFiles")
	@ResponseBody
	public List<DiffFile> diffFiles(
		@PathVariable String name,
		@PathVariable String branch,
		@PathVariable String commit)
		throws GitRepositoryException,
					 GitAPIException, 
					 IOException {

		return player
			.repository(name)
			.diffFilesFromCommit(
				branch, 
    	  commit
    	);
	}

	@GetMapping("/repo/{name}/{branch}/{commit}/diffInCommit")
	@ResponseBody
	public List<Map<String,String>> diffInCommit(
		@PathVariable String name,
		@PathVariable String branch,
		@PathVariable String commit)
		throws GitRepositoryException,
					 GitAPIException, 
					 IOException {

		return player
			.repository(name)
			.diffInCommit(
				branch, 
    	  commit
    	);
	}
	
	@GetMapping("/repo/download")
	@ResponseBody
	public Map<String, Boolean> download(
		@RequestBody Map<String,String> body) 
			throws GitRepositoryException {

		var repository = body.get("repository");
		var url = body.get("url");
		
		player
		.repository(repository)
		.download(url);
    
    var map = new HashMap<String,Boolean>();
    map.put("result", true);
    
    return map;
	}
}
