package ast;

import java.util.List;

import ast.exception.NonExecutedComparisionException;
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
	
	public ASTComparator(SourceCodeFile oldSource, SourceCodeFile newSource) {
		this.a = oldSource;
		this.b = newSource;
	}
	
	public void execute() {
		
		diff = comparator.compare(
			a.content(),
			b.content()
		);
		
		
		/*for(var mapping: diff.getMappingsComp()) {
			System.out.println(mapping.getFirst().toShortString());
			System.out.println(mapping.getFirst().getLabel());
			System.out.println(mapping.getFirst().getType());
		};*/
		
		//-1888507161
		
	}
	
	@SuppressWarnings("rawtypes")
	public List<Operation> editScript() 
		throws NonExecutedComparisionException {
		
		if(diff == null) 
			throw new NonExecutedComparisionException(
				"You must execute the comparision between sources files."
			);
		
		return diff.getAllOperations();
	}
	
	public CtType<?> source() {
		
		return comparator.getCtType(a.content());
	}
	
	public CtType<?> destination() {
		return comparator.getCtType(b.content());
	}
	
	
	
}
