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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Mapping(
			operation,
			o.getSrcNode(),
			o.getDstNode()
		);
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
	
	
	private static OperationType defineType(String type) 
		throws Exception {
		
		if(type.contains("INS")) return OperationType.INSERT;
		else if(type.contains("DEL")) return OperationType.DELETE;
		else if(type.contains("UPD")) return OperationType.UPDATE;
		else if(type.contains("MOV")) return OperationType.MOVE;
		else 
			throw new Exception(
				"Undefined type operation!" + " --- " + type
			);
	}
	
	
}
