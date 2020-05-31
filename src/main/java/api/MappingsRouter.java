package api;

import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import controller.MappingsController;

@RestController
public class MappingsRouter {

  @GetMapping("/mappings/{repository}/{branch}")
  @ResponseBody
  public Map<String, Boolean> mappingsToStore(
    @PathVariable String repository, 
    @PathVariable String branch)
      throws GitAPIException {

    //cd61fd2a70828030ccb3cf46d8719f8b204c52ed
    //está quebrando - parece o primeiro

    var controller = new MappingsController(repository, branch);
    var result = controller.extractMappingsToStore();
    return Map.of("result", result);
  }
}