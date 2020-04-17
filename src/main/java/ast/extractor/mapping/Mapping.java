package ast.extractor.mapping;

import ast.Mappable;
import ast.extractor.Change;

public abstract class Mapping implements Mappable {

	protected Change<?> source;
	protected Change<?> destination;

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
}
