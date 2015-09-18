package se.messme.actions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;
import se.messme.DBconnector;
import se.messme.Event;
import se.messme.dao.SessionDAO;
import se.messme.entities.Session;
import se.messme.entities.User;

public class AuthenticateLogin {

	public TreeMap<Event, Session> loginAuth(User user, String passwordFromLogin, String ipAddress) {	
		TreeMap<Event, Session> loginResponse = new TreeMap<Event, Session>();
		
		//null means that no user could be found in DB.
		if(user == null) {
			loginResponse.put(Event.WRONG_EMAIL, null);
		}
		else {
			//"password is the user input "user.getPassword()" is the hash from db
			if (BCrypt.checkpw(passwordFromLogin, user.getPassword())) {

				//Create a session for this user
				Session session = new Session();
				session.setDate(getCurrentDate());
				session.setEmail(user.getEmail());
				session.setFirstName(user.getFirstName());
				session.setLastName(user.getLastName());
				session.setIpAddress(ipAddress);
				session.setSessionUUID(UUID.randomUUID().toString());
				session.setUserId(user.getId());
				
				SessionDAO sessionDAO = new DBconnector();
				int result = sessionDAO.createSession(session);

				//Check for error while creating session
				if(result > 0) {
					loginResponse.put(Event.AUTH_CORRECT, session);
				}		
				else {
					loginResponse.put(Event.FAILED_TO_CREATE_SESSION, null);
				}
			}
			else {
				loginResponse.put(Event.WRONG_PASSWORD, null);
			}
		}
		return loginResponse;    	
	}
	
	private String getCurrentDate() {
		String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		return date;
	}
}
