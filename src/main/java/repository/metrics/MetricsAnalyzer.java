package repository.metrics;

import java.util.function.BiConsumer;

import com.github.mauricioaniche.ck.CKClassResult;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import repository.Commit;
import repository.Repository;

public class MetricsAnalyzer {
	
	private String repository;
	private Repository repo;
	
	public MetricsAnalyzer() { 

	}

	public void process(
		Commit commit, 
		BiConsumer<String,CKClassResult> consumer) {
		
		var items = repo.folder().getAbsolutePath().split("/");
		repository = items[items.length-1];

		new CKMetric(repo).calculate(
	  	commit,
	  	result -> {
	  		consumer.accept(
	  			filterFilename(result.getFile()), 
	  			result
	  		);
	  	}
	  );
	}

	private String filterFilename(String path) {
		var position = path
  		.lastIndexOf(repository) + repository.length() + 1;
  	
  	return path.substring(position);
	}

	public MetricsAnalyzer enableLogs(boolean value) {
		if(!value) Logger.getRootLogger().setLevel(Level.OFF);
		return this;
	}
	
	public MetricsAnalyzer repository(Repository r) {
    this.repo = r;
    return this;
  }

}
