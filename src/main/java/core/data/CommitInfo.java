package core.data;

public class CommitInfo {

	private String author;
	private String email;
	private String shortMessage;
	private String date;
	
	public CommitInfo(
		String author, 
		String email,
		String shortMessage, 
		String date) {

		this.author = author;
		this.email = email;
		this.shortMessage = shortMessage;
		this.date = date;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getShortMessage() {
		return shortMessage;
	}
	
	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
}
