package repository.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.google.gson.Gson;

import repository.Branch;
import repository.Commit;
import repository.Repository;
import util.Analyzer;
import util.Storage;

public class MetricsAnalyzer 
  implements Analyzer{

	private File folder;
	
	private String name;
	private Repository repo;
	
	public MetricsAnalyzer(
		String repositoryName, 
		Repository repo) {
	  
		this.name = repositoryName;
		this.repo = repo;
	}
	
	@Override
	public MetricsAnalyzer localFolder(File folder) {
	  this.folder = folder;
	  return this;
	}
	
	public MetricsAnalyzer enableLogs(boolean value) {
		if(!value) Logger.getRootLogger().setLevel(Level.OFF);
		return this;
	}
	
	@Override
	public void run() throws FileNotFoundException {
		
		if(folder == null) throw new FileNotFoundException(
			"You must define a local folder!"
		); 
		
	  var storage = new Storage(
	    folder.getAbsolutePath() + "/" + name
	  );
	  try {
	    
	    long startTime = System.currentTimeMillis();
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
      
      long endTime = System.currentTimeMillis();
      //That took 18.454 seconds
      //That took 16.267 seconds
      //That took 13.625 seconds
      System.out.println("That took " + (endTime - startTime)/1000.0 + " seconds");
      
    } catch (GitAPIException e) {
      System.err.println(e.getMessage());
    }
	}
	
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
	
	private void processing(
	  Storage storage, 
	  Commit commit) {
	  
	  var data = new MetricsData();
	  
	  new CKMetric()
	  .calculate(
	    commit.id(), 
	    result -> {
	    	
	    	var position = result
	    		.getFile()
	    		.lastIndexOf(name) + name.length() + 1;
	    	
	    	var text = result
	    		.getFile()
		      .substring(position);
	    	
	      data
	      .addFiles(
	      	text, 
	        result
	      );	      
	    }
	  );
	  
	  this.store(
	    storage,
	    commit.id().getName() + ".json",
	    data
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
	

}
