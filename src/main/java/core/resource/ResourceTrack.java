package core.resource;

import java.util.HashMap;

public class ResourceTrack {

  private static HashMap<ResourceType, Resource> list;

  public ResourceTrack() {
    
    initializeList();
  }


  private void initializeList() {

		if(list == null) {
			list = new HashMap<>();

			addResource(ResourceType.LIST_OF_BRANCHES, new ListOfBranchesResource());
			addResource(ResourceType.LIST_OF_COMMITS, new ListOfCommitsResource());
			addResource(ResourceType.METRICS_FILES, new MetricsFilesResource());
		}
	}

	public Resource get(ResourceType type) {

    return list.get(type);
  }

  private void addResource(ResourceType type, Resource resource) {
		
		list.put(type, resource);
	}
}