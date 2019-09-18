package ast.mapping;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;

public class TreeLinking {

	public TreeLinking() {
		
	}
	
	public void map(CtType<?> tree, CtElement element) {
	
		for(CtElement e: tree.asIterable()) {
			if(e.equals(element)) {
				
				System.out.println(e.toString());
				System.out.println(e.getPath());
				System.out.println(e.getRoleInParent().toString());
				System.out.println(e.getParent().getRoleInParent().toString());
			}
		}
	}
}
