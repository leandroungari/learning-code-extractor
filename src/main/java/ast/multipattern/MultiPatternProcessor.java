package ast.multipattern;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;

import ast.MappingsReader;
import ast.extractor.CommitMappings;
import ast.extractor.elements.ElementFactory;
import ast.extractor.mapping.MoveMapping;
import ast.extractor.mapping.UpdateMapping;
import ast.snapshot.Snapshot;

public class MultiPatternProcessor {

  private String repository;
  private String branch;
  private Integer from;
  private Integer to;

  private Snapshot snapshot;
  private List<String> commits;

  private Integer counter;
  private MultiPatternPersistor persistor;

  public MultiPatternProcessor(
    String repository, 
    String branch) throws GitAPIException {

    this.repository = repository;
    this.branch = branch;

    this.init();
  }

  public MultiPatternProcessor(
    String repository, 
    String branch,
    Integer from,
    Integer to) throws GitAPIException {
    
    this.repository = repository;
    this.branch = branch;
    this.from = from;
    this.to = to;

    this.init();
  }

  private void init() throws GitAPIException {
    this.snapshot = Snapshot.create(this.repository, this.branch);
    ElementFactory.setTempRepository(this.repository);

    this.counter = 0;
    this.persistor = new MultiPatternPersistor(this.repository);

    this.obtainingCommits();
  }

  Snapshot getSnapshot() {
    return snapshot;
  }

  public List<String> getCommits() {
    return commits;
  }

  void obtainingCommits() {

    if(from != null && to != null) {
      this.commits = snapshot.getBranch()
        .commits(from, to).stream()
        .map(item -> item.id().getName())
        .collect(Collectors.toList());

    } else {
      this.commits = snapshot.getBranch()
        .commits().stream()
        .map(item -> item.id().getName())
        .collect(Collectors.toList());

      this.commits.remove(0);
    }
    
  }

  public void identify() {
    var number = 1;
    var current = new ArrayList<MultiPattern>();
    for (var commit : this.commits) {
      System.err.println("> #" + number + " - " + commit);
      number++;
      var result = new ArrayList<MultiPattern>();
      var mappings = getMappingsFromCommit(commit).get();
      if (current.isEmpty()) {
        for (var mapping : mappings) {
          if (mapping instanceof MoveMapping || mapping instanceof UpdateMapping) {
            result.add(new MultiPattern(null, commit, mapping));
          }
        }
      } else {
        // identifica mapeamentos compatíveis com patterns
        for (var pattern : current) {
          var i = 0;
          while (i < mappings.size()) {
            var mapping = mappings.get(i);
            if (mapping instanceof MoveMapping || mapping instanceof UpdateMapping) {
              if (pattern.compatibility(mapping)) {

                pattern.add(commit, mapping);
                result.add(pattern);

                mappings.remove(i);
                break;
              } else {
                i++;
              }
            } else {
              mappings.remove(i);
            }
          }
        }

        // cria patterns para os mapeamentos que não tem pattern correspondente
        for (var mapping : mappings) {
          result.add(new MultiPattern(null, commit, mapping));
        }
      }
      // percorrer todos os patterns
      // você deve persistir todos (com tamanho maior ou igual a 2)
      // e remover os que
      // o último commit não é igual ao commit atual
      var i = 0;
      while (i < result.size()) {
        var pattern = result.get(i);
        if (pattern.getSize() > 1 && pattern.getLastCommit().equals(commit)) {
          if(pattern.getCode() == null) {
            pattern.setCode(++counter);
          }
          persistor.write(pattern);
          i++;
        } else if (!pattern.getLastCommit().equals(commit)) {
          result.remove(i);
        } else {
          i++;
        }
      }

      current = result;
    }
  }

  private CommitMappings getMappingsFromCommit(String id) {
    try {
      var reader = new MappingsReader(repository, branch);
      return reader.commit(id);
    } catch(Exception ex) {
      return new CommitMappings(repository, branch, id);
    }
  }

}