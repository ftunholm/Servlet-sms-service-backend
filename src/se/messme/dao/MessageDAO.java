package se.messme.dao;

import java.util.ArrayList;

import se.messme.Event;
import se.messme.entities.Message;

public interface MessageDAO {
	
	public Event createMessage(Message messageEntity);
	
	public Message[] getMessage(int userId);
	
	public ArrayList<Integer> checkForNewMessage();
	
	public Event changeMessageStatus(int messageId, int status);
}
