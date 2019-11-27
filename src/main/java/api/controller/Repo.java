package api.controller;

import java.util.List;
import java.util.Map;

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

@RestController
public class Repo {

	private static Player player = new Player();

	@GetMapping("/repo/{name}/branches")
	@ResponseBody
	public Branch[] allBranches(@PathVariable String name) {

		return player
			.<Branch[]>get(FeatureType.BRANCHES_OF_REPOSITORY)
			.setParams(name)
			.action();
	}

	@GetMapping("/repo/{name}/{branch}/commits")
	@ResponseBody
	public Commit[] allCommits(
		@PathVariable String name,
		@PathVariable String branch) {

		return player
			.<Commit[]>get(FeatureType.COMMITS_OF_BRANCH)
			.setParams(name, branch)
			.action();
	}

	@GetMapping("/repo/{name}/{branch}/{commit}/info")
	@ResponseBody
	public CommitInfo commitInfo(
		@PathVariable String name,
		@PathVariable String branch,
		@PathVariable String commit) {

		return player
			.<CommitInfo>get(FeatureType.COMMIT_INFO)
			.setParams(name, branch, commit)
			.action();
	}

	@GetMapping("/repo/{name}/{branch}/numberOfFiles")
	@ResponseBody
	public List<Integer> numberOfFiles(
		@PathVariable String repository, 
		@PathVariable String branch) {

		return player
			.<List<Integer>>get(FeatureType.NUMBER_OF_FILES_IN_COMMITS_BY_BRANCH)
			.setParams(repository, branch)
			.action();
	}

	@GetMapping("/repo/{name}/{branch}/contributors")
	@ResponseBody
	public List<Integer> numberOfContributors(
		@PathVariable String repository, 
		@PathVariable String branch) {

		return player
			.<List<Integer>>get(FeatureType.NUMBER_OF_CONTRIBUTORS_BY_BRANCH)
			.setParams(repository, branch)
			.action();
	}
	
	@GetMapping("/repo/{name}/{branch}/{commit}/diffFiles")
	@ResponseBody
	public List<DiffFile> diffFiles(
		@PathVariable String name,
		@PathVariable String branch,
		@PathVariable String commit) {

		return player
			.<List<DiffFile>>get(FeatureType.DIFF_FILES_IN_COMMIT)
			.setParams(name, branch, commit)
			.action();
	}

	@GetMapping("/repo/{name}/{branch}/{commit}/diffInCommit")
	@ResponseBody
	public List<Map<String,String>> diffInCommit(
		@PathVariable String name,
		@PathVariable String branch,
		@PathVariable String commit) {

		return player
			.<List<Map<String,String>>>get(FeatureType.DIFF_IN_COMMIT)
			.setParams(name, branch, commit)
			.action();
	}
	
	@GetMapping("/repo/download")
	@ResponseBody
	public Map<String, Boolean> download(@RequestBody Map<String,String> body)  {

		var repository = body.get("repository");
		var url = body.get("url");
		
		player
		.<Void>get(FeatureType.DOWNLOAD_REPOSITORY)
		.setParams(repository, url)
		.action();
    
    return Map.of("result", true);
	}
}
