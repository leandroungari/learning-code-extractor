package ast.extractor;

import java.util.List;

import ast.extractor.mapping.Mapping;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import spoon.reflect.declaration.CtType;

public class ASTComparator {

	private String before;
	private String after;
	private static AstComparator comparator = new AstComparator();

	private Diff diff;

	public ASTComparator(String before, String after) {
		this.before = before;
		this.after = after;

		diff = comparator.compare(before, after);
	}

	/**
	 * It obtains change mappings of diffs from two
	 * different source code
	 */
	public List<Mapping> mappings() {

		var generator = new GenerateMappings(diff);
		return generator.create();
	}

	public CtType<?> sourceTree() {

		return comparator.getCtType(before);
	}

	public CtType<?> destinationTree() {
		
		return comparator.getCtType(after);
	}

}
