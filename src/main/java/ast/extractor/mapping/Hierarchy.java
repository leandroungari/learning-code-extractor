package ast.extractor.mapping;

public interface Hierarchy {
  
	String toString();

	Boolean isParentOf(Mapping other);

	Boolean isChildOf(Mapping other);

	Integer getPriority();
}