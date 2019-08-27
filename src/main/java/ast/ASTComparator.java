package ast;

import java.util.List;

import ast.exception.NonExecutedComparisionException;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.Operation;

public class ASTComparator {

	private SourceCodeFile a;
	private SourceCodeFile b;
	private static AstComparator comparator = 
		new AstComparator();
	
	private Diff diff;
	
	public ASTComparator(SourceCodeFile a, SourceCodeFile b) {
		this.a = a;
		this.b = b;
	}
	
	public void execute() {
		
		diff = comparator.compare(
			a.content(),
			b.content()
		);
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
	
	
	
}
