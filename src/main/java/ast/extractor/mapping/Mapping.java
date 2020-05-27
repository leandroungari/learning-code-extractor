package ast.extractor.mapping;

import com.google.gson.JsonObject;

import core.Jsonify;
import ast.extractor.elements.Element;
import ast.snapshot.CommitSnapshot;

public abstract class Mapping implements Jsonify<Mapping>, Comparable<Mapping> {

	protected Element<?> source;
	protected Element<?> destination;

	protected CommitSnapshot snapshot;

	protected String oldFile;

  protected String newFile;

	public void setSnapshot(CommitSnapshot snap) {
		this.snapshot = snap;
	}

	public Boolean isSameType(Mapping other) {

		return getClass() == other.getClass();
	}

	public Element<?> source() {
		return source;
	}

	public Element<?> destination() {
		return destination;
	}
	
	public void setOldFile(String oldFile) {
    this.oldFile = oldFile;
  }

  public void setNewFile(String newFile) {
    this.newFile = newFile;
  }

  public String getOldFile() {
    return oldFile;
  }
  
  public String getNewFile() {
    return newFile;
  }

	@Override
	public abstract String toString();

	public abstract Boolean isParentOf(Mapping other);

	public abstract Boolean isChildOf(Mapping other);

	@Override
	public boolean equals(Object obj) {
		var other = (Mapping) obj;
		return toString().equals(other.toString());
	}

	protected static String pathOfElement(
		JsonObject object, 
		String element) {

		var item = object.get(element).getAsJsonObject();
    return item.get("path").getAsString();
	}

	protected abstract Integer getPriority();

	@Override
	public int compareTo(Mapping other) {
		if(this.getPriority() < other.getPriority()) {
			return -1;
		} else if(this.getPriority() > other.getPriority()) {
			return 1;
		}
		return 0;
	}

	/*protected Boolean verifyCorrespondence(
    Integer totalOfElements, 
    Integer numberOfMatches) {

    if (totalOfElements == numberOfMatches) {
      return true;
    } else if (totalOfElements != 0) {
      System.err.println("Correspondência parcial");
    }
    return false;
	}
	
	protected List<CtElement> dependencies(
		CtElement target) {
			
		return Change
      .createChange(target)
      .getCompleteChange();
	}
	*/
}
