package ast.extractor;

import java.util.List;

import ast.Mappable;
import spoon.reflect.declaration.CtElement;

public abstract class Mapping implements Mappable {

	protected List<Change<?>> source;
	protected List<Change<?>> destination;

	public Boolean isSameType(Mapping other) {

		return getClass() == other.getClass();
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

	protected Boolean verifyCorrespondence(
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
}
