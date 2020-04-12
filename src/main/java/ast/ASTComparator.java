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
		return verifyParentRelationship(mappings());
	}

	private List<Modification> createModificationResult(List<Mapping> list) {
		var result = new ArrayList<Modification>();
		if(!list.isEmpty()) result.add(new Modification(list.get(0)));
		return result;
	}

	private List<Modification> verifyParentRelationship(List<Mapping> list) {

		var result = createModificationResult(list);
		for(var unsetted: list) {
			for(var modification: result) {
				for(var setted: modification.getMappings()) {
					if(unsetted.isSameType(setted)) {
						//verificar cada tipo de modificação
						// e verificar se um elemento é pai ou filho do outro
						if(unsetted.type() == OperationType.INSERT) {
							if() {

							}
						}
						else if(list.get(unsetted).type() == OperationType.DELETE) {

						}
						else {

						}
					}
				}
			}
		}

		return result;
	}



	//comparar todos os elementos contra todos para verificar se um é pai do 
	//outro, o retorno ele pode acontecer antes do elemento pai e causar
	//falha na implementação atual

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
