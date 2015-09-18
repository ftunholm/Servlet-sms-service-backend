package se.messme.entities;

public class Message {
	
	private String[] phoneNumbers;
	private String message, creationDate, sentDate;
	private int userId, status, id; // status can be 0, 1 or 2
	
	
	public String[] getPhoneNumbers() {
		return phoneNumbers;
	}
	
	public void setPhoneNumbers(String[] phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getSentDate() {
		return sentDate;
	}
	
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
