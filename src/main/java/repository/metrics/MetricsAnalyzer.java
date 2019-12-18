package repository.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.github.mauricioaniche.ck.CKClassResult;
import com.google.gson.Gson;

import core.data.MetricsData;
import repository.Branch;
import repository.Commit;
import repository.Repository;
import util.Storage;

public class MetricsAnalyzer {

	private File folder;
	
	private String name;
	private Repository repo;
	
	public MetricsAnalyzer() { }
	
	

	public MetricsAnalyzer localFolder(File folder) {
	  this.folder = folder;
	  return this;
	}
	
	public MetricsAnalyzer enableLogs(boolean value) {
		if(!value) Logger.getRootLogger().setLevel(Level.OFF);
		return this;
	}
	
	/*public void run() throws FileNotFoundException {
		
		if(folder == null) throw new FileNotFoundException(
			"You must define a local folder!"
		); 
		
	  var storage = new Storage(
	    folder.getAbsolutePath() + "/" + name
	  );
	  try {
	    
	    var list = repo
      .branches()
      .stream()
      .map(branch -> 
        CompletableFuture
        .runAsync(
           () -> this.processing(storage, branch)
        )
      )
      .collect(Collectors.toList());
	    
	    CompletableFuture.allOf(
	      list.toArray(new CompletableFuture[list.size()])
	    ).join();
      
      
    } catch (GitAPIException e) {
      System.err.println(e.getMessage());
    }
	}*/
	
	private void processing(
	  Storage storage, 
	  Branch branch) {

	  var commits = branch
	  .commits();
	  
	  for(Integer i = 0; i < commits.size(); i+= 4) {
	    
	    var list = commits
	    .subList(
	      i, 
	      (i+4 < commits.size() ? i+4 : commits.size())
	    )
	    .stream()
	    .map(commit ->
	      CompletableFuture
	      .runAsync(() -> 
	        this.processing(
	          storage.folder(branch.id().getName()),
	          commit
	      ))
	    )
	    .collect(Collectors.toList());
	      
	    CompletableFuture
	    .allOf(list.toArray(new CompletableFuture[list.size()]))
	    .join();
	  }

	}
	
	private String filterFilename(String path) {
		var position = path
  		.lastIndexOf(name) + name.length() + 1;
  	
  	return path
      .substring(position);
	}
	
	private void processing(
	  Storage storage, 
	  Commit commit) {
	  
	  var data = new MetricsData();
	  
	  new CKMetric(repo).calculate(
	    commit, 
	    result -> {
	    	
	    	var text = filterFilename(result.getFile());
	      data.addFiles(text, result);	      
	    }
	  );
	  
	  this.store(
	    storage,
	    commit.id().getName() + ".json",
	    data
	  );
	}
	
	public void process(
		Commit commit, 
		BiConsumer<String,CKClassResult> consumer) {
		
		new CKMetric(repo)
	  .calculate(
	  	commit,
	  	result -> {
	  		consumer.accept(
	  			filterFilename(result.getFile()), 
	  			result
	  	);
	  	}
	  );
	}
	
	private void store(
	  Storage storage, 
	  String filename, 
	  MetricsData obj) {
	  
	  var gson = new Gson();
    var data = gson.toJson(obj);
    
    storage.file(filename, data);
	}
	
	public MetricsData get(
	  String branch,
	  String commit) {
	
	  var gson = new Gson();
	  
	  var data = new Storage(
	    folder.getAbsolutePath() + "/" + name
	  )
	  .folder(branch)
	  .get(commit + ".json");
	  
	  return gson.fromJson(data, MetricsData.class);	  
	}

  public MetricsAnalyzer repository(Repository r) {
    this.repo = r;
    return this;
  }

}
