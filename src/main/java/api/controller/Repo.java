package api.controller;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import repository.Repository;
import repository.exception.GitRepositoryException;

@RestController
public class Repo {

  @GetMapping("/repo/{repositoryName}/allBranches")
  @ResponseBody
  public List<String> allBranches(
    @PathVariable String repositoryName) 
    throws GitRepositoryException, 
            GitAPIException {
    
    var repo = Repository.create(new File(
      "/home/leandroungari/Documents/data/repositories/" +
      repositoryName
    ));
    
    return repo
      .branches()
      .stream()
      .map(branch -> branch.name())
      .collect(Collectors.toList());
  }
  
}
