package ast;

import java.util.ArrayList;
import java.util.List;

import ast.mapping.Mapping;
import ast.mapping.Modification;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.Operation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;

public class ASTComparator {

	private String before;
	private String after;
	private static AstComparator comparator = new AstComparator();

	private List<Modification> result;
	
	private Diff diff;
	
	public ASTComparator(String before, String after) {
		this.before = before;
		this.after = after;

		diff = comparator.compare(before,after);
		result = new ArrayList<Modification>();
	}
	
	public List<Mapping> mappings() {
		
		var list = new ArrayList<Mapping>();
		for(Operation<?> o: diff.getAllOperations()) {
			list.add(new Mapping(o));
		}

		return list;
	}

	public List<Modification> extract() {
		
		result.clear();
		Modification modification = null;
		for(var mapping: mappings()) {
			modification = extractByType(modification, mapping);
		}

		if(modification != null) {
			result.add(modification);
		}

		return result;
	}

	private Modification extractByType(Modification modification, Mapping mapping) {
		if(modification == null) {
			return new Modification(mapping);
		}
		else if(mapping.type() == OperationType.INSERT) {
			return extractInsertion(modification, mapping);
		}
		else if(mapping.type() == OperationType.MOVE || 
						mapping.type() == OperationType.UPDATE) {
			return extractUpdateMove(modification, mapping);
		}
		else {
			return extractDeletion(modification, mapping);
		}
	}

	private Modification extractInsertion(Modification modification, Mapping mapping) {
	
		Boolean hasParent = false;
		for(var current: result) {
			if(current.get(0).type() == OperationType.INSERT ||
				containsParent(current.destinations(), mapping.destination()) != -1) {
				hasParent = true;
				break;
			}
		}
		if(!hasParent) {
			result.add(modification);
			return new Modification(mapping);
		}
		return modification;
	}

	private Modification extractUpdateMove(Modification modification, Mapping mapping) {

		for(var current: result) {
			var position = containsParent(
				current.sources(), 
				mapping.source()
			);

			if(position != -1 && 
				 mapping.type() != modification.last().type()	&&
				 mapping
				 .destination()
				 .hasParent(modification.destinations().get(position))
			) {
				result.add(modification);
				return new Modification(mapping);
			}
		}
		
		return modification;
	}

	private Modification extractDeletion(Modification modification, Mapping mapping) {
		Boolean hasParent = false;
		for(var current: result) {
			if(current.get(0).type() == OperationType.DELETE ||
				containsParent(current.sources(), mapping.source()) != -1) {
				hasParent = true;
				break;
			}
		}
		if(!hasParent) {
			result.add(modification);
			return new Modification(mapping);
		}
		return modification;
	}

	private Integer containsParent(List<CtElement> list, CtElement element) {
	
		for(var i = 0; i < list.size(); i++) {
			if(element.hasParent(list.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	public CtType<?> sourceTree() {
		
		return comparator.getCtType(before);
	}
	
	public CtType<?> destinationTree() {
		return comparator.getCtType(after);
	}
	
	
	
}
