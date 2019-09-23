package repository.commit;

import java.util.Date;

import org.eclipse.jgit.lib.ObjectId;

public class CommitInfo {	

	private ObjectId id;
	private String name;
	private String email;
	private String shortMessage;
	private String date;
	
	private CommitInfo() {
		
	}
	
	public static CommitInfo create(ObjectId id) {
		
		var info = new CommitInfo();
		info.id = id;
		return info;
	}
	
	public ObjectId id() {
		return id;
	}
	
	public String authorName() {
		
		return name;
	}
	
	public String email() {
		return email;
	}
	
	public String date() {
		return date;
	}
	
	public String shortMessage() {
		return shortMessage;
	}
	
	public CommitInfo setAuthorName(String name) {
		this.name = name;
		return this;
	}
	
	public CommitInfo setEmail(String email) {
		this.email = email;
		return this;
	}
	
	public CommitInfo setShortMessage(String message) {
		this.shortMessage = message;
		return this;
	}
	
	public CommitInfo setDate(Date date) {
		
		this.date = date.toInstant().toString();
		return this;
	}
}
