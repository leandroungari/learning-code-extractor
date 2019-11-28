package core.executor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;

import core.Player;
import core.data.Commit;
import core.data.DiffFile;
import core.resource.Resource;
import core.resource.ResourceType;
import repository.GitFactory;
import repository.Repository;
import repository.commit.CommitInfo;
import repository.exception.GitRepositoryException;

public class RepositoryExecutor {

	private Player player;
	
	public RepositoryExecutor(Player p) {
		
		player = p;
	}
	
	public void diffFiles(
		String repository,
		String branch,
		String commit) 
			throws GitRepositoryException, 
						 GitAPIException {
		
		var repo = Repository.create(new File(
			player.settings().folderOfRepositories() + "/" +
			repository
		));
		
		var diffList = repo
			.branch(ObjectId.fromString(branch))
			.commit(ObjectId.fromString(commit))
			.diff();
		
    var result = diffList
      .stream()
			.map(diff -> {
				return new DiffFile(diff.type(), diff.oldPath(), diff.newPath());
      })
      .collect(Collectors.toList());
      		
		var resource = Resource
			.create(repository, ResourceType.DIFF_FILES)
			.branch(branch);
		
		var listOfCommits = resource.read(Commit[].class);
		
		for(var i = 0; i < listOfCommits.length; i++) {
			if(listOfCommits[i]
				.getId()
				.getName()
				.equals(commit)) {

				listOfCommits[i].setDiffFiles(result);
				break;
			}
		}
		
		resource.write(listOfCommits);
	}

	public List<Map<String,String>> diffsInCommit(
		String repository,
		String branch, 
		String commit) 
			throws GitRepositoryException, 
						 GitAPIException {
		
		var repo = Repository.create(new File(
			player.settings().folderOfRepositories() + "/" +
			repository
		));

		var ref = repo
      .branch(ObjectId.fromString(branch))
      .commit(ObjectId.fromString(commit));

    var list = ref.diff();
    var factory = new GitFactory();

    System.out.println(list);

    var result = new ArrayList<Map<String,String>>();
    for(var item: list) {
      result.add(factory.contentOfDiffCommit(ref,item));
	  }
		return result;
	}
}
