package ast;

import java.util.ArrayList;
import java.util.List;

import ast.mapping.Mapping;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.Operation;
import spoon.reflect.declaration.CtType;

public class ASTComparator {

	private SourceCodeFile a;
	private SourceCodeFile b;
	private static AstComparator comparator = 
		new AstComparator();
	
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

	
	@SuppressWarnings("rawtypes")
	private List<Operation> editScript() {

		return diff.getAllOperations();
	}
	
	public CtType<?> sourceTree() {
		
		return comparator.getCtType(a.content());
	}
	
	public CtType<?> destinationTree() {
		return comparator.getCtType(b.content());
	}
	
	
	
}
