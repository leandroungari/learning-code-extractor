package command;

import java.io.IOException;

import com.beust.jcommander.Parameter;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.merger.MergeCluster;

public class MergeCommand implements Command {

  @Parameter(names = { "-repo" })
  private String repository;

  @Parameter(names = { "-branch" })
  private String branch;

  public void run() {

    try {
      var merger = new MergeCluster(repository, branch);
      merger.merge();
    } catch (GitAPIException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}