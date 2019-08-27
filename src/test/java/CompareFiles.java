import org.junit.Test;

import ast.ASTComparator;
import ast.SourceCodeFile;
import ast.exception.NonExecutedComparisionException;
import gumtree.spoon.diff.operations.Operation;

public class CompareFiles {

	
	@Test
	public void run() throws NonExecutedComparisionException {
		
		var file1 = "package org;\n" + 
				"import org.animals.Dog;\n" + 
				"\n" + 
				"\n" + 
				"public class DogManager {\n" + 
				"\n" + 
				"	private Dog dog;\n" + 
				"	public DogManager(Dog aDog) {\n" + 
				"		this.dog = aDog;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public void doStuff() {\n" + 
				"		this.dog.barkBark(this);\n" + 
				"		\n" + 
				"		int age = dog.getAge();\n" + 
				"		int sum = 0;\n" + 
				"		for (int i = 0; i < age; i++) {\n" + 
				"			System.out.println(i);\n" + 
				"			sum += i;\n" + 
				"		}\n" + 
				"		sum -= dog.magicNumber;\n" + 
				"		dog.takeABreath();\n" + 
				"	}\n" + 
				"\n" + 
				"}";
		
		var file2 = "\n" + 
				"package org;\n" + 
				"import org.animals.Dog;\n" + 
				"\n" + 
				"\n" + 
				"public class DogManager {\n" + 
				"\n" + 
				"	private Dog dog;\n" + 
				"	public DogManager(Dog aDog) {\n" + 
				"		this.dog = aDog;\n" + 
				"	}\n" + 
				"	\n" + 
				"	public void doStuff() {\n" + 
				"		barkBark(this.dog);\n" + 
				"		\n" + 
				"		int age = dog.getAge();\n" + 
				"		int sum = 0;\n" + 
				"		for (int i = 0; i < age; i++) {\n" + 
				"			System.out.println(i);\n" + 
				"			sum += i;\n" + 
				"		}\n" + 
				"		sum -= dog.magicNumber;\n" + 
				"		dog.takeABreath();\n" + 
				"	}\n" + 
				"\n" + 
				"	public void barkBark(Dog dog) {\n" + 
				"		System.out.println(\"ruff\");\n" + 
				"		System.out.println(\"ruff\");\n" + 
				"		dog.takeABreath();\n" + 
				"		System.out.println(\"ruff\");\n" + 
				"		System.out.println(\"ruff\");\n" + 
				"		System.out.println(\"ruff\");\n" + 
				"	}\n" + 
				"\n" + 
				"}";
		
		var sc2 = SourceCodeFile.create("DogManager.java", file2);
		var sc1 = SourceCodeFile.create("DogManager.java", file1);
		
		var comparator = new ASTComparator(sc2, sc1);
		comparator.execute();
		
		System.out.println("=== Sequência de Edição ===");
		
		for(Operation o: comparator.editScript()) {
			//System.out.println(o.toString());
			System.out.println(o.getAction());
			System.out.println(o.getSrcNode());
			System.out.println(o.getDstNode());
			o.getSrcNode().getParent().
			
		}
		
	}
	
}
