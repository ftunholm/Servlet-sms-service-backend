package se.messme.actions;

import se.messme.DBconnector;
import se.messme.Event;
import se.messme.dao.UserDAO;

public class Register {

	public Event registerNewUser(String email, String password, String firstName,
			String lastName, String birthDay, String birthMonth, String birthYear) {
		
		UserDAO userDAO = new DBconnector();     	   	

    	int result = userDAO.addUser(email, password, firstName, lastName, 
				birthDay, birthMonth, birthYear);
    	
		if(result > 0) {
			return Event.REGISTER_USER_SUCCESS; 
		}
		else {
			return Event.REGISTER_USER_FAILED;
		}		
	}
}
