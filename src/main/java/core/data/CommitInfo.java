package core.data;

import storage.Storable;

public class CommitInfo implements Storable<CommitInfo>{

  private String id;
	private String name;
	private String email;
	private String shortMessage;
	private String fullMessage;
  private String date;
  
  public CommitInfo() {

  }

  /**
   * @return the date
   */
  public String getDate() {
    return date;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @return the fullMessage
   */
  public String getFullMessage() {
    return fullMessage;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  
  /**
   * @return the shortMessage
   */
  public String getShortMessage() {
    return shortMessage;
  }

  /**
   * @param date the date to set
   */
  public void setDate(String date) {
    this.date = date;
  }
  
  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }
  /**
   * @param fullMessage the fullMessage to set
   */
  public void setFullMessage(String fullMessage) {
    this.fullMessage = fullMessage;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @param shortMessage the shortMessage to set
   */
  public void setShortMessage(String shortMessage) {
    this.shortMessage = shortMessage;
  }

  @Override
  public String[] attributes() {
    
    return new String[]{
      "id","name","email","shortMessage",
      "fullMessage","date"
    };
  }

  @Override
  public String[] write() {
    
    return new String[]{
      getId(), getName(), getEmail(), getShortMessage(),
      getFullMessage(), getDate()
    };
  }

  @Override
  public CommitInfo read(String[] values) {
    
    var info = new CommitInfo();
    info.setId(values[0]);
    info.setName(values[1]);
    info.setEmail(values[2]);
    info.setShortMessage(values[3]);
    info.setFullMessage(values[4]);
    info.setDate(values[5]);
    return info;
  }
}