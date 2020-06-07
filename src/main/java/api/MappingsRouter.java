package api;

import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import controller.mappings.MappingsController;

@RestController
public class MappingsRouter {

  @GetMapping("/mappings/{repository}/{branch}")
  @ResponseBody
  public Map<String, Boolean> mappingsToStore(
    @PathVariable String repository, 
    @PathVariable String branch)
      throws GitAPIException {

    var controller = new MappingsController(repository, branch);
    var result = controller.extractMappingsToStore();
    return Map.of("result", result);
  }
}