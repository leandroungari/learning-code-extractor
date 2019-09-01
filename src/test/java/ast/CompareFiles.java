package ast;
import org.junit.Test;

public class CompareFiles {

	
	@Test
	public void run() {
		
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
		
		var comparator = new ASTComparator(sc1, sc2);
		
		System.out.println("=== Source tree ===");
		System.out.println(comparator.sourceTree().toString());
		System.out.println("=== Destination tree ===");
		System.out.println(comparator.destinationTree().toString());
		
		System.out.println("=== Mappings ===");
		for(var mapping: comparator.mappings()) {
			
			System.out.println(mapping.type().toString());
			System.out.println(mapping.source().pattern());
			System.out.println(mapping.destination().pattern());
		}
		
	}
	
}
