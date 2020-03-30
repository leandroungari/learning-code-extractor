package ast.mapping;

import ast.OperationType;
import gumtree.spoon.diff.operations.Operation;
import spoon.reflect.declaration.CtElement;

public class Mapping {

	private OperationType operation;
	private CtElement source;
	private CtElement destination;

	public Mapping(Operation<?> o) {

		operation = defineType(o.getAction().toString());

		if (operation == OperationType.INSERT) {
			destination = o.getSrcNode();
		} 
		else if (operation == OperationType.UPDATE || 
						 operation == OperationType.MOVE) {
			source = o.getSrcNode();
			destination = o.getDstNode();
		} 
		else {
			source = o.getSrcNode();
		}
	}

	public OperationType type() {
		return operation;
	}

	public CtElement source() {
		return source;
	}

	public CtElement destination() {
		return destination;
	}

	private static OperationType defineType(String type) {

		if (type.contains("INS"))
			return OperationType.INSERT;
		else if (type.contains("DEL"))
			return OperationType.DELETE;
		else if (type.contains("UPD"))
			return OperationType.UPDATE;
		else
			return OperationType.MOVE;
	}

	@Override
  public String toString() {

    if (operation == OperationType.INSERT) {
			return "null\n=>\n" + destination().toString();
		} 
		else if (operation == OperationType.UPDATE || 
						 operation == OperationType.MOVE) {
			return source().toString() + "\n=>\n" + destination().toString();
		} 
		else {
			return source().toString() + "\n=>\nnull";
		}
  }
}
