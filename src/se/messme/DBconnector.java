package se.messme;

import com.mysql.jdbc.Connection;

import com.mysql.jdbc.Driver;

import org.mindrot.jbcrypt.BCrypt;

import se.messme.dao.*;
import se.messme.entities.*;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DBconnector implements UserDAO, SessionDAO, ContactDAO, GroupDAO, MessageDAO, DeviceDAO {


	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://178.62.103.238/db_messme_develop";
	private String sqlPassword = "testpass";
	private String username = "messme_db";
	private Connection connection;
	private String query;
	private PreparedStatement preparedStatement;    
	private ResultSet resultSet = null;



	@Override
	public User getUser(String email) {
		query = "SELECT * FROM users WHERE mail = (?)";
		User user = null;

		try {
            Class.forName(driver).newInstance();

            connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);

			resultSet = preparedStatement.executeQuery();		

			if(!resultSet.next()) {
				//user will be null
				return user;
			}
			else {
				user = new User();
				user.setEmail(resultSet.getString(Constants.EMAIL));
				user.setPassword(resultSet.getString(Constants.PASSWORD));
				user.setFirstName(resultSet.getString(Constants.FIRST_NAME));
				user.setLastName(resultSet.getString(Constants.LAST_NAME));
				user.setId(resultSet.getInt(Constants.ID));
				user.setBirthDay(resultSet.getString(Constants.BIRTH_DAY));
				user.setBirthMonth(resultSet.getString(Constants.BIRTH_MONTH));
				user.setBirthYear(resultSet.getString(Constants.BIRTH_YEAR));
			}
			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        return user;
	}

	@Override
	public int addUser(String email, String password, String firstName,
			String lastName, String birthDay, String birthMonth, String birthYear) {

		String pw_hash = BCrypt.hashpw(password, BCrypt.gensalt());

		int result = 0;

		query = "INSERT INTO users (mail, pw_hash, firstname, lastname, birth_day, birth_month, birth_year)"
				+ " VALUES(?, ?, ?, ?, ?, ?, ?)";

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, pw_hash);
			preparedStatement.setString(3, firstName);
			preparedStatement.setString(4, lastName);
			preparedStatement.setString(5, birthDay);
			preparedStatement.setString(6, birthMonth);
			preparedStatement.setString(7, birthYear);

			result = preparedStatement.executeUpdate();	

			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }

        return result;
	}

	@Override
	public void deleteUser(String email) {

	}

	@Override
	public void updateUser(String email) {

	}

	@Override
	public Session getSessionBySessionUUID(String sessionUUID) {
		query = "SELECT * FROM sessions WHERE session_id = (?)";
		Object session = null;

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, sessionUUID);

			resultSet = preparedStatement.executeQuery();		

			if(!resultSet.next()) {
				//session will be null
				return (Session)session;
			}
			else {
				session = Translator.translateFromJson(resultSet.getString(Constants.SESSION_DATA), Session.class);
			}
			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        return (Session)session;
	}

	@Override
	public Session getSessionByUserId(int userId) {
		query = "SELECT * FROM sessions WHERE user_id = (?)";
		Object session = null;

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userId);

			resultSet = preparedStatement.executeQuery();		

			if(!resultSet.next()) {
				//session will be null
				return (Session)session;
			}
			else {
				session = Translator.translateFromJson(resultSet.getString(Constants.SESSION_DATA), Session.class);
			}
			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        return (Session)session;
	}

	@Override
	public int createSession(Session session) {
		int result = 0;

		query = "INSERT INTO sessions (session_id, session_data)"
				+ " VALUES(?, ?)";

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, session.getSessionUUID());
			preparedStatement.setString(2, Translator.translateToJson(session));

			result = preparedStatement.executeUpdate();	

			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }

        return result;
	}

	@Override
	public boolean checkSession(String sessionUUID) {

		query = "SELECT * FROM sessions WHERE session_id = (?)";

		boolean hasSession = false;

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, sessionUUID);

			resultSet = preparedStatement.executeQuery();

			if(!resultSet.next()) {
				hasSession = false;
			}
			else {
				hasSession = true;
			}
			connection.close();

		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        return hasSession;
	}

	@Override
	public Event createContact(String firstName, String lastName,
			String phoneNumber, String email, int userId, int groupId) {

		int result = 0;

		query = "INSERT INTO contacts (firstname, lastname, phone, mail, user_id, group_id)"
				+ " VALUES(?, ?, ?, ?, ?, ?)";

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			preparedStatement.setString(3, phoneNumber);
			preparedStatement.setString(4, email);
			preparedStatement.setInt(5, userId);
			preparedStatement.setInt(6, groupId);

			result = preparedStatement.executeUpdate();	

			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }

        if (result > 0) {
			return Event.ADD_CONTACT_SUCCESS;
		}
		else {
			return Event.ADD_CONTACT_FAILED;
		}
	}

	@Override
	public ArrayList<Contact> getContacts(int userId) {

		ArrayList<Contact> temp = new ArrayList<Contact>();

		query = "SELECT * FROM contacts WHERE user_id = (?) ORDER BY id DESC";

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userId);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Contact c = new Contact();
				c.setContactId(resultSet.getInt(Constants.CONTACT_ID));
				c.setEmail(resultSet.getString(Constants.EMAIL));
				c.setFirstName(resultSet.getString(Constants.FIRST_NAME));
				c.setLastName(resultSet.getString(Constants.LAST_NAME));
				c.setGroupId(resultSet.getInt(Constants.GROUP_ID));
				c.setPhoneNumber(resultSet.getString(Constants.PHONE_NUMBER));
				c.setUserId(resultSet.getInt(Constants.CONTACT_USER_ID));
				temp.add(c);
			}

			connection.close();

		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        return temp;
	}

	@Override
	public ArrayList<Contact> getContactsByGroupId(int groupId) {
		ArrayList<Contact> temp = new ArrayList<Contact>();

		query = "SELECT * FROM contacts WHERE group_id = (?)";

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, groupId);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Contact c = new Contact();
				c.setContactId(resultSet.getInt(Constants.CONTACT_ID));
				c.setEmail(resultSet.getString(Constants.EMAIL));
				c.setFirstName(resultSet.getString(Constants.FIRST_NAME));
				c.setLastName(resultSet.getString(Constants.LAST_NAME));
				c.setGroupId(resultSet.getInt(Constants.GROUP_ID));
				c.setPhoneNumber(resultSet.getString(Constants.PHONE_NUMBER));
				c.setUserId(resultSet.getInt(Constants.CONTACT_USER_ID));
				temp.add(c);
			}

			connection.close();

		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        return temp;
	}

	@Override
	public Event editContact(int contactId) {
		return null;
	}

	@Override
	public Event removeContact(int contactId) {

		int result = 0;

		query = "DELETE FROM contacts WHERE id = (?)";
		
		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, contactId);

			result = preparedStatement.executeUpdate();	

			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }

        if (result > 0) {
			return Event.REMOVE_CONTACT_SUCCESS;
		}
		else {
			return Event.REMOVE_CONTACT_FAILED;
		}
	}

	@Override
	public Event addContactToGroup(int groupId, int contactId) {

		int result = 0;

		query = "UPDATE contacts SET group_id = (?) WHERE id = (?)";

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, groupId);
			preparedStatement.setInt(2, contactId);


			result = preparedStatement.executeUpdate();	

			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }

        if (result > 0) {
			return Event.ADD_CONTACT_TO_GROUP_SUCCESS;
		}
		else {
			return Event.ADD_CONTACT_TO_GROUP_FAILED;
		}
	}

	@Override
	public Event createGroup(String groupName, int userId) {
		int result = 0;

		query = "INSERT INTO phone_group (group_name, user_id)"
				+ " VALUES(?, ?)";

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, groupName);
			preparedStatement.setInt(2, userId);


			result = preparedStatement.executeUpdate();	

			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }

        if (result > 0) {
			return Event.CREATE_GROUP_SUCCESS;
		}
		else {
			return Event.CREATE_GROUP_FAILED;
		}
	}

	@Override
	public ArrayList<Group> getAllGroups(int userId) {
		ArrayList<Group> temp = new ArrayList<Group>();

		query = "SELECT * FROM phone_group WHERE user_id = (?)";

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userId);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Group g = new Group();
				g.setGroupId(resultSet.getInt(Constants.ID));
				g.setGroupName(resultSet.getString(Constants.GROUP_NAME));
				g.setUserId(resultSet.getInt(Constants.GROUP_USER_ID));
				temp.add(g);
			}

			connection.close();

		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        return temp;
	}

	@Override
	public Event updateGroup(int groupId) {
		return null;
	}

	@Override
	public Event deleteGroup(int groupId) {
		return null;
	}

	@Override
	public Event createMessage(Message messageEntity) {
		int result = 0;

		query = "INSERT INTO messages (message, contacts, status, created_time, user_id)"
				+ " VALUES(?, ?, ?, ?, ?)";

		String jsonContacts = Translator.translateToJson(messageEntity.getPhoneNumbers());

		try {
            Class.forName(driver).newInstance();


			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, messageEntity.getMessage());
			preparedStatement.setString(2, jsonContacts);
			preparedStatement.setInt(3, messageEntity.getStatus());
			preparedStatement.setString(4, messageEntity.getCreationDate());
			preparedStatement.setInt(5, messageEntity.getUserId());

			result = preparedStatement.executeUpdate();	

			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }

        if (result > 0) {
			return Event.CREATE_MESSAGE_SUCCESS;
		}
		else {
			return Event.CREATE_MESSAGE_FAILED;
		}
	}

	@Override
	public Message[] getMessage(int userId) {

		ArrayList<Message> temp = new ArrayList<Message>();
		Message[] messages = null;

		query = "SELECT * FROM messages WHERE user_id = (?)";

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userId);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				if(resultSet.getInt(Constants.MESSAGE_STATUS) == 0) {
					Message m = new Message();
					m.setSentDate(resultSet.getString(Constants.MESSAGE_SENT_TIME));
					m.setCreationDate(resultSet.getString(Constants.MESSAGE_CREATION_DATE));
					m.setMessage(resultSet.getString(Constants.MESSAGE_GET_MESSAGE));
					m.setPhoneNumbers(Translator.translateFromJsonToArray(resultSet.getString(Constants.MESSAGE_CONTACTS)));
					m.setId(resultSet.getInt(Constants.MESSAGE_ID));
					m.setStatus(resultSet.getInt(Constants.MESSAGE_STATUS));
					m.setUserId(resultSet.getInt(Constants.MESSAGE_USER_ID));
					temp.add(m);
					break;
				}
			}

			connection.close();

			messages = new Message[temp.size()];
			for (int i = 0; i < temp.size(); i++) {
				messages[i] = temp.get(i);
			}

		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        return messages;
	}

	@Override
	public ArrayList<Integer> checkForNewMessage() {
		ArrayList<Integer> userIds = new ArrayList<Integer>();

		query = "SELECT * FROM messages";

		try {
            Class.forName(driver).newInstance();
			Connection connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				if(resultSet.getInt(Constants.MESSAGE_STATUS) == 0) {
					userIds.add(resultSet.getInt(Constants.MESSAGE_USER_ID));
				}
			}
			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        return userIds;
	}

	@Override
	public Device getDevice(int userId) {
		query = "SELECT * FROM devices WHERE user_id = (?)";
		Device device = new Device();

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userId);

			resultSet = preparedStatement.executeQuery();
			resultSet.next();

			device.setId(resultSet.getInt(Constants.DEVICE_ID));
			device.setName(resultSet.getString(Constants.DEVICE_NAME));
			device.setPlatform(resultSet.getString(Constants.DEVICE_PLATFORM));
			device.setPushKey(resultSet.getString(Constants.DEVICE_PUSH_KEY));
			device.setUserId(resultSet.getInt(Constants.DEVICE_USER_ID));

			connection.close();

		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        return device;
	}

	@Override
	public Event createDevice(Device device) {

		int result = 0;

		if(checkIfDeviceExists(device.getUserId())) {
			query = "UPDATE devices SET name = ?, platform = ?, push_key = ? WHERE user_id = ?";
		}
		else {
			query = "INSERT INTO devices (name, platform, push_key, user_id)"
					+ " VALUES(?, ?, ?, ?)";
		}

		try {
            Class.forName(driver).newInstance();

            connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);			
			preparedStatement.setString(1, device.getName());
			preparedStatement.setString(2, device.getPlatform());
			preparedStatement.setString(3, device.getPushKey());
			preparedStatement.setInt(4, device.getUserId());

			result = preparedStatement.executeUpdate();	

			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }

        if (result > 0) {
			return Event.CREATE_DEVICE_SUCCESS;
		}
		else {
			return Event.CREATE_DEVICE_FAILED;
		}
	}

	private boolean checkIfDeviceExists(int userId) {

		query = "SELECT * FROM devices WHERE user_id = (?)";
		boolean deviceExists = false;

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userId);

			resultSet = preparedStatement.executeQuery();

			if(!resultSet.next()) {
				deviceExists = false;
			}
			else {
				deviceExists = true;
			}

			connection.close();

		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        return deviceExists;
	}

	@Override
	public Event changeMessageStatus(int messageId, int status) {

		int result = 0;
		query = "UPDATE messages SET status = ? WHERE id = ?";

		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);		
			preparedStatement.setInt(1, status);
			preparedStatement.setInt(2, messageId);

			result = preparedStatement.executeUpdate();	

			connection.close();
		}
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }


        if (result > 0) {
			return Event.CHANGE_MESSAGE_STATUS_SUCCESS;
		}
		else {
			return Event.CHANGE_MESSAGE_STATUS_FAILED;
		}
	}

	@Override
	public Event removeSession(String sessionUUID) {
		int result = 0;

		query = "DELETE FROM sessions WHERE session_id = (?)";
		
		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, sessionUUID);

			result = preparedStatement.executeUpdate();	

			connection.close();
		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }

        if (result > 0) {
			return Event.REMOVE_SESSION_SUCCESS;
		}
		else {
			return Event.REMOVE_SESSION_FAILED;
		}
	}

	@Override
	public Event removeSessionByTime() {
		
		Event result = null;
		
		query = "SELECT * FROM sessions";
		Session session;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		
    	Date sessionCreatedDate = null;

    	Date currentDate = new Date();
    	currentDate.getTime();
    	
		try {
            Class.forName(driver).newInstance();

			connection = (Connection) DriverManager.getConnection(url, username, sqlPassword);

			preparedStatement = connection.prepareStatement(query);

			resultSet = preparedStatement.executeQuery();
        	
			while (resultSet.next()) {
				session = (Session) Translator.translateFromJson(resultSet.getString(Constants.SESSION_DATA), Session.class);
				try {
					sessionCreatedDate = sdf.parse(session.getDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				System.out.print("  current date: " + currentDate.getTime());
				System.out.print("  session created date:   " + sessionCreatedDate.getTime());
				System.out.print("  difference in days:  " + (currentDate.getTime() - sessionCreatedDate.getTime()) / (24 * 60 * 60 * 1000));
				if(((currentDate.getTime() - sessionCreatedDate.getTime()) / (24 * 60 * 60 * 1000)) >= 1) {
					result = removeSession(session.getSessionUUID());
				}
			}

			connection.close();

		} 
		catch (SQLException e) {
			ExceptionLogger.log(e);
		} catch (ClassNotFoundException e) {
			ExceptionLogger.log(e);
        } catch (InstantiationException e) {
        	ExceptionLogger.log(e);
        } catch (IllegalAccessException e) {
        	ExceptionLogger.log(e);
        }
		
		return result;
	}
}

