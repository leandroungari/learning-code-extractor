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

  private Snapshot snapshot;
  private List<String> commits;

  private Integer counter;
  private MultiPatternPersistor persistor;

  public MultiPatternProcessor(String repository, String branch) throws GitAPIException {
    this.repository = repository;
    this.branch = branch;

    this.snapshot = Snapshot.create(this.repository, this.branch);
    ElementFactory.setTempRepository(this.repository);

    this.commits = snapshot.getBranch().commits().stream().map(item -> item.id().getName())
        .collect(Collectors.toList());

    this.commits.remove(0);

    this.counter = 0;
    this.persistor = new MultiPatternPersistor(this.repository);
  }

  Snapshot getSnapshot() {
    return snapshot;
  }

  List<String> getCommits() {
    return commits;
  }

  public void identify() {

    var current = new ArrayList<MultiPattern>();
    for (var commit : this.commits) {
      var result = new ArrayList<MultiPattern>();
      var mappings = getMappingsFromCommit(commit).get();
      if (current.isEmpty()) {
        for (var mapping : mappings) {
          if (mapping instanceof MoveMapping || mapping instanceof UpdateMapping) {
            result.add(new MultiPattern(++counter, commit, mapping));
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
          result.add(new MultiPattern(++counter, commit, mapping));
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
    var reader = new MappingsReader(repository, branch);
    return reader.commit(id);
  }

}