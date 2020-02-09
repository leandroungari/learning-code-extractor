package commandline.executors;

import java.util.Arrays;
import java.util.Map;

import commandline.Executor;
import core.Player;
import core.data.Branch;
import core.data.MetricsData;
import core.features.FeatureType;


/**
 * Improve this
 * 
 * This implementation only process the entire repository
 * 
 * @author leandroungari
 *
 */
public class Metrics implements Executor {

	private String repository;
	private String branch;
	private Integer from;
	private Integer to;
	private boolean isHelper;
	
	private Player player = new Player();
	
	@Override
	public void run() {
		
		if(isHelper) {
			helper();
		}
		else if(branch == null) {

			var branches = player
				.<Branch[]>get(FeatureType.BRANCHES_OF_REPOSITORY)
				.setParams(repository)
				.action();

			for(var b: branches) {
				player
      	.<Map<String,MetricsData>>get(FeatureType.METRICS_IN_RANGE_COMMITS)
      	.setParams(repository,b.getId())
      	.action();
			}

		}
		else if (from == null) {
			player
      .<Map<String,MetricsData>>get(FeatureType.METRICS_IN_RANGE_COMMITS)
      .setParams(repository,branch)
      .action();
		}
		else {
			player
      .<Map<String,MetricsData>>get(FeatureType.METRICS_IN_RANGE_COMMITS)
      .setParams(repository,branch,from.toString(),to.toString())
      .action();
		}
	}

	@Override
	public void param(String... params) {
		if("-h".equals(params[0])) {
			isHelper = true;
		}
		else {
			repository = params[0];
			System.out.println("aqui");
			System.out.println(Arrays.toString(params));
			branch = params.length > 1 ? params[1] : null;
			from = params.length > 2 ? Integer.valueOf(params[2]) : null;
			to = params.length > 3 ? Integer.parseInt(params[3]) : null;
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
