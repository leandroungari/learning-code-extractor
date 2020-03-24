package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ast.mapping.Mapping;
import ast.mapping.Modification;
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

	private List<Modification> result;
	
	private Diff diff;
	
	public ASTComparator(SourceCodeFile oldSource, SourceCodeFile newSource) {
		a = oldSource;
		b = newSource;

		diff = comparator.compare(a.content(),b.content());
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
			if(modification == null) {
				modification = new Modification(mapping);
			}
			else if(mapping.type() == OperationType.INSERT) {
				modification = extractInsertion(modification, mapping);
			}
			else if(mapping.type() == OperationType.MOVE || 
							mapping.type() == OperationType.UPDATE
			) {
				modification = extractUpdateMove(modification, mapping);
			}
			else {
				modification = extractDeletion(modification, mapping);
			}
		}

		return result;
	}

	private Modification extractInsertion(Modification modification, Mapping mapping) {
		var sources = getDestinations(modification);
		var mappings = modification.getMappings();
		if(mapping.type() != mappings.get(mappings.size()-1).type() ||
			 containsParent(sources, mapping.destination()) == -1 
		) {
			result.add(modification);
			return new Modification(mapping);
		}
		return modification;
	}

	private Modification extractUpdateMove(Modification modification, Mapping mapping) {
		var sources = getSources(modification);
		var destinations = getDestinations(modification);

		var position = containsParent(
			sources, 
			mapping.source()
		);

		var mappings = modification.getMappings();
		if(position == -1 || 
			 mapping.type() != mappings.get(mappings.size()-1).type()	||
			 !mapping
			 .destination()
			 .hasParent(destinations.get(position))
		) {
			result.add(modification);
			return new Modification(mapping);
		}
		return modification;
	}

	private Modification extractDeletion(Modification modification, Mapping mapping) {
		var sources = getSources(modification);
		var mappings = modification.getMappings();
		if(mapping.type() != mappings.get(mappings.size()-1).type() ||
			 containsParent(sources, mapping.source()) == -1
		) {
			result.add(modification);
			return new Modification(mapping);
		}
		return modification;
	}

	private List<CtElement> getSources(Modification modification) {
		return modification
			.getMappings()
			.stream()
			.map(item -> item.source())
			.collect(Collectors.toList());
	}

	private List<CtElement> getDestinations(Modification modification) {
		return modification
			.getMappings()
			.stream()
			.map(item -> item.destination())
			.collect(Collectors.toList());
	}

	private Integer containsParent(List<CtElement> list, CtElement element) {
	
		for(var i = 0; i < list.size(); i++) {
			System.out.println("\n======");
			System.out.println(list.get(i));
			System.out.println(element);
			System.out.println(element.hasParent(list.get(i)));
			System.out.println(list.get(i).getPath().toString());
			if(element.hasParent(list.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	public CtType<?> sourceTree() {
		
		return comparator.getCtType(a.content());
	}
	
	public CtType<?> destinationTree() {
		return comparator.getCtType(b.content());
	}
	
	
	
}
