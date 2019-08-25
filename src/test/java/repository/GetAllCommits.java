package repository;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.Test;

import repository.exception.UndefinedLocalRepositoryPathException;

public class GetAllCommits {

  @Test
  public void run() 
    throws InvalidRemoteException, 
           TransportException, 
           UndefinedLocalRepositoryPathException, 
           GitAPIException, IOException {
    
    var repo = new Repository(
    	"https://github.com/danilofes/refactoring-toy-example.git"
    );
    
    if(repo.cloneRepository("/home/leandroungari/refactoring-toy-example")) {
      System.out.println("Repositório clonado!");
    }
    else {
    	System.out.println("O repositório já existia!");
    }
    
    var list = repo.allCommitsIDs();
    list.stream().forEach(System.out::println);
    
    assertEquals(list.size(), 65);
  }
}
