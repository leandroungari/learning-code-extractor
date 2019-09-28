package api.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import repository.Repository;
import repository.commit.CommitInfo;
import repository.exception.GitRepositoryException;

@RestController
public class Repo {

  @GetMapping("/repo/{repositoryName}")
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
      .map(branch -> branch.id().getName())
      .collect(Collectors.toList());
  }
  
  @GetMapping("/repo/{repositoryName}/{branchId}")
  @ResponseBody
  public List<String> allCommits(
    @PathVariable String repositoryName,
    @PathVariable String branchId) 
    throws GitRepositoryException, 
            GitAPIException, 
            IOException {
    
    var repo = Repository.create(new File(
      "/home/leandroungari/Documents/data/repositories/" +
      repositoryName
    ));
    
    return repo
      .branch(ObjectId.fromString(branchId))
      .commits()
      .stream()
      .map(commit -> commit.id().getName())
      .collect(Collectors.toList());
  }
  
  @GetMapping("/repo/{repositoryName}/{branch}/{commit}")
  @ResponseBody
  public CommitInfo commitInfo(
    @PathVariable String repositoryName,
    @PathVariable String branch,
    @PathVariable String commit) 
    throws GitRepositoryException, 
            GitAPIException, 
            IOException {
    
    var repo = Repository.create(new File(
      "/home/leandroungari/Documents/data/repositories/" +
      repositoryName
    ));
      
    return repo
      .branch(ObjectId.fromString(branch))
      .commit(commit)
      .info();
  }
  
}
