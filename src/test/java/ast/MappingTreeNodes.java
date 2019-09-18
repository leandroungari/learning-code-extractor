package ast;

import org.junit.Before;
import org.junit.Test;

import ast.mapping.OperationType;
import ast.mapping.TreeLinking;

public class MappingTreeNodes {
	
	private ASTComparator ast;

	@Before
	public void beforeRun() {
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
		
		var sc1 = SourceCodeFile.create(
			"DogManager.java", 
			file1
		);
		
		var sc2 = SourceCodeFile.create(
			"DogManager.java", 
			file2
		);
		
		ast = new ASTComparator(sc1, sc2);
	}
	
	@Test
	public void run() {
		
		var linking = new TreeLinking();
		for(var map: ast.mappings()) {
			
			if(map.type() == OperationType.INSERT) {
				linking.map(ast.destinationTree(), map.source().get());
			}
		}
	}
}
