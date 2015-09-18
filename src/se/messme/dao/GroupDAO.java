package se.messme.dao;

import java.util.ArrayList;

import se.messme.Event;
import se.messme.entities.Group;

public interface GroupDAO {
	
	public Event createGroup(String groupName, int userId);
	
	public ArrayList<Group> getAllGroups(int userId);
	
	public Event updateGroup(int groupId);
	
	public Event deleteGroup(int groupId);

}
