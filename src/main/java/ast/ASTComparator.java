package ast;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import ast.exception.NonExecutedComparisionException;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.Operation;
import spoon.reflect.declaration.CtElement;

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
	
	public CtElement source() {
		
		return diff.commonAncestor();
	}
	
	public CtElement destination() {
		return diff.changedNode();
	}
	
	
	
}
