package ast.mapping;

import gumtree.spoon.diff.operations.Operation;
import spoon.reflect.declaration.CtElement;

public class Mapping {
	
	private OperationType operation;
	private CtElement source;
	private CtElement destination;
	
	private CtElement current;

	private Mapping(
		OperationType t, CtElement s, CtElement d
		) {
		
		this.operation = t;
		this.source = s;
		this.destination = d;
		
		this.current = this.source;
	}
	
	public static Mapping create(
			@SuppressWarnings("rawtypes") 
			Operation o
		) {
		
		OperationType operation = null;
		try {
			operation = defineType(o.getAction().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(operation == OperationType.INSERT) {
			return new Mapping(operation,null,o.getSrcNode());
		}
		else if(operation == OperationType.UPDATE || 
						operation == OperationType.MOVE) {
			return new Mapping(operation,o.getSrcNode(),o.getDstNode());
		}
		else {
			return new Mapping(operation,null,o.getSrcNode());
		}
	}
	
	public OperationType type() {
		return operation;
	}
	
	public Mapping source() {
		current = source;
		return this;
	}
	
	public Mapping destination() {
		current = destination;
		return this;
	}
	
	public String pattern() { 
		return (
			current != null ?
			current.descendantIterator().next().toString():
			""
		);
	}
	
	public String role() {
		
		return (
			current != null ?
			current.getRoleInParent().toString() :
			""
		);
	}
	
	public CtElement get() {
		return current;
	}
	
	
	private static OperationType defineType(String type) {
		
		if(type.contains("INS")) return OperationType.INSERT;
		else if(type.contains("DEL")) return OperationType.DELETE;
		else if(type.contains("UPD")) return OperationType.UPDATE;
		else return OperationType.MOVE;
	}
}
