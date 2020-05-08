package ast.extractor.mapping;

import ast.extractor.Jsonify;
import ast.extractor.elements.Element;
import ast.snapshot.Snapshot;

public abstract class Mapping implements Jsonify<Mapping> {

	protected Element<?> source;
	protected Element<?> destination;

	protected Snapshot snapshot;

	public void setSnapshot(Snapshot snap) {
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

	@Override
	public abstract String toString();

	public abstract Boolean isParentOf(Mapping other);

	public abstract Boolean isChildOf(Mapping other);

	@Override
	public boolean equals(Object obj) {
		var other = (Mapping) obj;
		return toString().equals(other.toString());
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

	public static <T extends Mapping> T create(JsonObject object) {
		//TODO
		throw new Error("Not implemented yet! Serialization of mappings");
	}*/
}
