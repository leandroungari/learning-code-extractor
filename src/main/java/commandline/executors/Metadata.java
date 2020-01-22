package commandline.executors;

import java.util.List;

import commandline.Executor;
import core.Player;
import core.data.Branch;
import core.data.Commit;
import core.data.DiffFile;
import core.features.FeatureType;


/**
 * Improve this
 * 
 * This implementation only process the entire repository
 * 
 * @author leandroungari
 *
 */
public class Metadata implements Executor {

	private String repository;
	
	private boolean isHelper;
	
	private Player player = new Player();
	
	@Override
	public void run() {
		
		if(isHelper) {
			helper();
		}
		else {

			var branches = player
			.<Branch[]>get(FeatureType.BRANCHES_OF_REPOSITORY)
			.setParams(repository)
			.action();

			for(var branch: branches) {
				
				var commits = player
				.<Commit[]>get(FeatureType.COMMITS_OF_BRANCH)
				.setParams(repository, branch.getId())
				.action();

				for(var commit: commits) {

					player
					.<List<DiffFile>>get(FeatureType.DIFF_FILES_IN_COMMIT)
					.setParams(repository, branch.getId(), commit.getId())
					.action();
				}
			}
		}	
	}

	@Override
	public void param(String... params) {
		if("-h".equals(params[0])) {
			isHelper = true;
		}
		else {
			repository = params[0];
		}
	}

	@Override
	public void helper() {
		System.out.println("This command analyses a repository to extract code metrics");
		System.out.println("metrics [repository] [type] [rangeOfCommits]");
		System.out.println(" - [repository] Name of repository");
		System.out.println(" - [branch] All branches or id");
		System.out.println(" - [from] Initial commit");
		System.out.println(" - [to] Last commit");
	}

}
