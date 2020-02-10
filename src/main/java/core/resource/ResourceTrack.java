package core.resource;

import java.util.HashMap;

import core.resource.resources.ListOfBranchesResource;
import core.resource.resources.ListOfCommitsResource;
import core.resource.resources.MetricsFilesResource;

public class ResourceTrack {

  private static HashMap<ResourceType, Resource<?>> list;

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

	@SuppressWarnings("unchecked")
	public <T> Resource<T> get(ResourceType type) {

    return (Resource<T>) list.get(type);
  }

  private <T> void addResource(ResourceType type, Resource<T> resource) {
		
		list.put(type, resource);
	}
}