package ast;

import java.util.ArrayList;
import java.util.List;

import ast.mapping.Mapping;
import ast.mapping.OperationType;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.Operation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;

public class ASTComparator {

	private SourceCodeFile a;
	private SourceCodeFile b;
	private static AstComparator comparator = new AstComparator();
	
	private Diff diff;
	
	public ASTComparator(
		SourceCodeFile oldSource, 
		SourceCodeFile newSource
		) {
		a = oldSource;
		b = newSource;

		diff = comparator.compare(
			a.content(),
			b.content()
		);
	}
	
	public List<Mapping> mappings() {
		
		var list = new ArrayList<Mapping>();
		
		editScript().forEach(o -> {
			list.add(Mapping.create(o));
		});
		
		return list;
	}

	public List<CtElement> getNewModifiedElements() {

		var result = new ArrayList<CtElement>();
		CtElement item;

		for(var mapping: mappings()) {

			item = getNewElement(mapping);
			
			if(item != null && !containsParent(result, item)) {
				result.add(item);
			}
		}

		return result;
	}

	private CtElement getNewElement(Mapping mapping) {

		CtElement item = null;
		if(mapping.type() == OperationType.INSERT) {
			item = mapping.source().get();
		}
		else if(mapping.type() == OperationType.UPDATE) {
			item = mapping.destination().get();
		}
		else if(mapping.type() == OperationType.MOVE) {
			item = mapping.source().get();
		}

		return item;
	}

	public List<CtElement> getOldModifiedElements() {

		var result = new ArrayList<CtElement>();
		CtElement item = null;
		
		for(var mapping: mappings()) {

			item = getOldElement(mapping);

			if(item != null && !containsParent(result, item)) {
				result.add(item);
			}
		}

		return result;
	}

	private CtElement getOldElement(Mapping mapping) {
		
		CtElement item = null;
		
		if(mapping.type() == OperationType.DELETE) {
			item = mapping.source().get();
		}
		else if(mapping.type() == OperationType.UPDATE) {
			item = mapping.source().get();
		}
		else if(mapping.type() == OperationType.MOVE) {
			item = mapping.source().get();
		}

		return item;
	}

	private boolean containsParent(List<CtElement> list, CtElement element) {

		for(var parent: list) {
			if(element.hasParent(parent)) {
				return true;
			}
		}
		return false;
	}

	
	@SuppressWarnings("rawtypes")
	public List<Operation> editScript() {

		return diff.getAllOperations();
	}
	
	public CtType<?> sourceTree() {
		
		return comparator.getCtType(a.content());
	}
	
	public CtType<?> destinationTree() {
		return comparator.getCtType(b.content());
	}
	
	
	
}
