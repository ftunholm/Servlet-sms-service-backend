package se.messme.dao;

import se.messme.entities.User;

public interface UserDAO {
	
	public User getUser(String email);

	
	public int addUser(String email, String password, String firstName,
			String lastName, String birthDay, String birthMonth, String birthYear);

	public void deleteUser(String email);
	
	public void updateUser(String email);

}
