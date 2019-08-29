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
		
		/*comparator.getCtType(a.content()).asIterable().forEach(e -> {
			System.out.println(e.toString());
		});*/
		
		
		
		/*for(var a: diff.getMappingsComp()) {
			System.out.println("//");
			System.out.println(a.first.toShortString());
			System.out.println(a.getFirst().getType() + " -- " + a.getFirst().getLabel());
			System.out.println(a.second.toShortString());
			
		}*/
		
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
