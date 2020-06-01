package api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import controller.mappings.ClusteringController;

@RestController
public class ClusteringRouter {
  
  @GetMapping("/cluster/{repository}/{branch}")
  @ResponseBody
  public Map<String,Boolean> clustering(
    @PathVariable String repository,
    @PathVariable String branch
  ) {
    var controller = new ClusteringController(repository, branch);
    var result = controller.clusterToStore();
    return Map.of("result", result);
  }

}