package ast;

public class SourceCodeFile {

	private String content;
	private String name;
	private String version;
	
	public static SourceCodeFile create(
			String name, String content
		) {
		
		return new SourceCodeFile(name, content, "-1");
	}
	
	public static SourceCodeFile create(
			String name, String content, String version
		) {
		
		return new SourceCodeFile(name, content, version);
	}
	
	public SourceCodeFile(
			String name, String content, String version
		) {
		
		this.content = content;
		this.version = version;
		this.name = name;
	}
	
	public String name() {
		return name;
	}
	
	public String content() {
		return content;
	}
	
	public String version() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
}
