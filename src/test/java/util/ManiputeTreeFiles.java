package util;

import org.junit.Before;
import org.junit.Test;

public class ManiputeTreeFiles {

	private Storage storage;
	@Before
	public void before() {
		storage = new Storage("/home/leandroungari/Documents/Teste");
	}
	
	@Test
	public void createFile() {
		
		
		storage.file("teste.txt", "Olá Mundo");
	}
	
	@Test
	public void createFolderAndFiles() {
		
		storage
		.folder("books")
		.file("Hobbit.txt", "In a role in the ground there lived a hobbit!");
	}
	
	@Test 
	public void createFolderAndFilesTwo() {
		
		storage
		.folder("movies")
		.folder("Lord of Rings")
		.file("Two Towers.txt", "The power of Isengard and Mordor");
	}
	
}
