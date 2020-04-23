package api.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ast.CommitModifications;
import core.Player;
import core.features.FeatureType;

@RestController()
public class Modifications {

  private static Player player = new Player();

  @GetMapping("/modifications/extract/{repository}/{branch}")
  @ResponseBody
  public Map<String,Boolean> extract(
    @PathVariable String repository,
    @PathVariable String branch) {

    player
      .<CommitModifications>get(FeatureType.EXTRACT_MODIFICATIONS)
      .setParams(repository, branch)
      .action();
    
    return Map.of("result", true);
  }
}