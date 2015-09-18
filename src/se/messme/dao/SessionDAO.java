package se.messme.dao;

import se.messme.Event;
import se.messme.entities.Session;

public interface SessionDAO {

	public boolean checkSession(String sessionUUID);
	public Session getSessionByUserId(int userId);
	public Session getSessionBySessionUUID(String sessionUUID);
	public int createSession(Session session);
	public Event removeSession(String sessionUUID);
	public Event removeSessionByTime();
}
