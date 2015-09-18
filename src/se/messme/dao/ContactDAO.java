package se.messme.dao;

import java.util.ArrayList;

import se.messme.Event;
import se.messme.entities.Contact;

public interface ContactDAO {
	
	public Event createContact(String firstName, String lastName, String phoneNumber, 
			String email, int userId, int groupId);
	
	public ArrayList<Contact> getContacts(int userId);
	
	public ArrayList<Contact> getContactsByGroupId(int groupId);

	public Event editContact(int contactId);
	
	public Event removeContact(int contactId);
	
	public Event addContactToGroup(int groupId, int contactId);
}
