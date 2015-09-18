package se.messme.router;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import se.messme.ClearSessionCron;
import se.messme.DBconnector;
import se.messme.Event;
import se.messme.ExceptionLogger;
import se.messme.Translator;
import se.messme.actions.AuthenticateLogin;
import se.messme.actions.Register;
import se.messme.dao.ContactDAO;
import se.messme.dao.DeviceDAO;
import se.messme.dao.GroupDAO;
import se.messme.dao.MessageDAO;
import se.messme.dao.SessionDAO;
import se.messme.dao.UserDAO;
import se.messme.entities.Contact;
import se.messme.entities.Device;
import se.messme.entities.Group;
import se.messme.entities.Message;
import se.messme.entities.Session;
import se.messme.entities.User;

public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RequestDispatcher dispatcher = null;
	private HttpSession httpSession;
	private ContactDAO contactDAO;
	private GroupDAO groupDAO;
	private MessageDAO messageDAO;
	private SessionDAO sessionDAO;
	private UserDAO userDAO;
	private DeviceDAO deviceDAO;
	private boolean sessionAccepted;

	public Controller() {		
		super();

		sessionAccepted = false;

		new HTTPrequestForGCM();
		new ClearSessionCron();

		contactDAO = new DBconnector();
		groupDAO = new DBconnector();
		sessionDAO = new DBconnector();
		userDAO = new DBconnector();
		messageDAO = new DBconnector();
		deviceDAO = new DBconnector();
	}

	private boolean checkSessionForPostRequests(String sessionUUID) {

		if(sessionDAO.checkSession(sessionUUID)) {
			return true;
		}
		else {
			return false;
		}	
	}	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		httpSession = request.getSession();
		
		try {
			if (sessionDAO.checkSession((String)httpSession.getAttribute("user_session"))) {
				sessionAccepted = true;
			}
			else {
				sessionAccepted = false;
			}
		}
		catch(Exception e) {
			dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
		}


		String requestPath = request.getRequestURI().substring(request.getContextPath().length());

		switch(requestPath) {
		
		case "/logout":
			if(sessionAccepted) {
				Event removeSessionResponse = sessionDAO.removeSession((String) httpSession.getAttribute("user_session"));
				if(removeSessionResponse == Event.REMOVE_SESSION_SUCCESS) {
					dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
				}
				else {
					//TODO: handle this?
				}
			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}
			break;

		//This request is for the phone app to collect the messages to be sent.
		case "/getmessages":
			if(sessionAccepted) {
				Session sessionEntity = sessionDAO.getSessionBySessionUUID((String) httpSession.getAttribute("user_session"));					
				Message[] message = messageDAO.getMessage(sessionEntity.getUserId());

				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				//The array will only contain one object
				response.getWriter().write(Translator.translateToJson(message[0]));					
			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}
			break;

		case "/index":	
			if(sessionAccepted) {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/logged_in.jsp");
			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}
			break;

		case "/login":			
			if(sessionDAO.checkSession((String) httpSession.getAttribute("user_session"))) {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/logged_in.jsp");
			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}				
			break;

		case "/contacts":
			if(sessionAccepted) {
				Session sessionEntity = sessionDAO.getSessionBySessionUUID((String) httpSession.getAttribute("user_session"));
				
				ArrayList temp = contactDAO.getContacts(sessionEntity.getUserId());
				String[] jsonContacts = new String[temp.size()];
				
				for(int i = 0; i < temp.size(); i++) {
					jsonContacts[i] = Translator.translateToJson(temp.get(i));
				}
				//Pass an ArrayList<Contact> with the request
				request.setAttribute("contactList", jsonContacts);

				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/contacts.jsp");
			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}			
			break;

		case "/groups":
			if(sessionAccepted) {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/groups.jsp");

				Session sessionEntity = sessionDAO.getSessionBySessionUUID((String) httpSession.getAttribute("user_session"));					

				ArrayList<Group> groups = groupDAO.getAllGroups(sessionEntity.getUserId());
				ArrayList<Contact> contacts = contactDAO.getContacts(sessionEntity.getUserId());

				String[] jsonGroups = new String[groups.size()];
				String[] jsonContacts = new String[contacts.size()];

				for (int i = 0; i < groups.size(); i++) {
					jsonGroups[i] = Translator.translateToJson(groups.get(i));	
				}
				for (int j = 0; j < contacts.size(); j++) {
					jsonContacts[j] = Translator.translateToJson(contacts.get(j));
				}

				request.setAttribute("groups", jsonGroups);	
				request.setAttribute("contacts", jsonContacts);						
			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}	
			break;

		case "/register":	
			dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/register.jsp");
			break;

		case "/messages":				
			if(sessionAccepted) {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/messages.jsp");	

				Session sessionEntity = sessionDAO.getSessionBySessionUUID((String) httpSession.getAttribute("user_session"));					

				ArrayList<Group> groups = groupDAO.getAllGroups(sessionEntity.getUserId());
				ArrayList<Contact> contacts = contactDAO.getContacts(sessionEntity.getUserId());

				String[] jsonGroups = new String[groups.size()];
				String[] jsonContacts = new String[contacts.size()];

				for (int i = 0; i < groups.size(); i++) {
					jsonGroups[i] = Translator.translateToJson(groups.get(i));	
				}
				for (int j = 0; j < contacts.size(); j++) {
					jsonContacts[j] = Translator.translateToJson(contacts.get(j));
				}

				request.setAttribute("groups", jsonGroups);	
				request.setAttribute("contacts", jsonContacts);		
			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}						
			break;
		default:
			break;
		}
		if(!request.getRequestURI().equals("/MessMeBackend/getmessages")) {
			dispatcher.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String email = "";
		String password = "";
		String firstName = "";
		String lastName = "";
		String birthYear = "";
		String birthMonth = "";
		String birthDay = "";
		String ipAddress = request.getRemoteAddr();

		httpSession = request.getSession();
		

		if (checkSessionForPostRequests((String) httpSession.getAttribute("user_session"))) {
			sessionAccepted = true;
		}
		else {
			sessionAccepted = false;
		}

		switch(request.getServletPath()) {
		
		case "/removecontact":			
			if(sessionAccepted) {	
				String contactToRemove = request.getParameter("contact_id");
				
				Event removeContactResponse = contactDAO.removeContact(Integer.parseInt(contactToRemove));
				
				//TODO handle event....
			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}			
			break;

		case "/changemessagestatus":
			String status = "";
			String message_id = "";

			if(sessionAccepted) {	
				try {
					status = request.getParameter("status");
					message_id = request.getParameter("message_id");
				}
				catch(Exception e) {
					ExceptionLogger.log(e);
				}

				Event changeMessageStatusResponse = messageDAO.changeMessageStatus(Integer.parseInt(message_id), Integer.parseInt(status));

				if(changeMessageStatusResponse == Event.CHANGE_MESSAGE_STATUS_SUCCESS) {
					response.setStatus(200);

				}
				else {
					response.setStatus(201);
				}
			}
			else {
				response.setStatus(404);
			}
			break;

		case "/login":

			if(sessionAccepted) {		
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/logged_in.jsp");				
			}
			else {						
				email = request.getParameter("email");
				password = request.getParameter("password");
				TreeMap<Event, Session> loginResponse;

				User user = userDAO.getUser(email);

				//This will check if the user input matches with db and it will also create a session for the logged in user.
				loginResponse = new AuthenticateLogin().loginAuth(user, password, ipAddress);

				if (loginResponse.firstKey() == Event.AUTH_CORRECT) {
					httpSession.setAttribute("user_session", loginResponse.firstEntry().getValue().getSessionUUID());
					response.setStatus(200);

					try {
						String user_agent = (String) request.getParameter("User-Agent");
						String push_key = (String) request.getParameter("Push-Key");
						
						if(user_agent == null || push_key == null) {
							dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/logged_in.jsp");
						}
						else {
							
							Device device = new Device();

							device.setName(user_agent);
							device.setPlatform(user_agent);
							device.setPushKey(push_key);
							device.setUserId(sessionDAO.getSessionBySessionUUID((String) httpSession.getAttribute("user_session")).getUserId());

							Event createDeviceResponse = deviceDAO.createDevice(device);

							if(createDeviceResponse != Event.CREATE_DEVICE_SUCCESS) {							
								//TODO: handle this response
							}
						}
					}
					catch(Exception e) {
						ExceptionLogger.log(e);
					}					
				}
				else if (loginResponse.firstKey() == Event.WRONG_EMAIL) {
					response.setStatus(401);
					dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
					request.setAttribute("errorMsg", "Fel e-postadress, vänligen försök igen.");	 		
				}
				else if (loginResponse.firstKey() == Event.WRONG_PASSWORD) {
					response.setStatus(402);
					dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
					request.setAttribute("errorMsg", "Fel lösenord, vänligen försök igen.");
				}
				else if (loginResponse.firstKey() == Event.FAILED_TO_CREATE_SESSION) {
					response.setStatus(403);
					dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
					request.setAttribute("errorMsg", "Det gick inte att upprätta en session för"
							+ "din inloggning, vänligen försök igen senare.");
				}
			}

			break;

		case "/register":
			email = request.getParameter("email");
			password = request.getParameter("password");
			String repassword = request.getParameter("repassword");
			firstName = request.getParameter("firstName");
			lastName = request.getParameter("lastName");
			birthYear = request.getParameter("birthYear");
			birthMonth = request.getParameter("birthMonth");
			birthDay = request.getParameter("birthDay");	

			if(userDAO.getUser(email) == null) {
				if(password.equals(repassword)) {				
					Event registerResponse = new Register().registerNewUser(email, password, firstName, lastName, 
							birthDay, birthMonth, birthYear);
					if(registerResponse == Event.REGISTER_USER_SUCCESS) {
						dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
					}
				}
				else {
					//TODO: handle the password mismatch ....
				}
			}
			else {
				request.setAttribute("emailExistsError", "E-postadressen är redan registrerad");	
			}
			break;

		case "/addcontact":

			if(sessionAccepted) {
				email = request.getParameter("email");
				String phoneNumber = request.getParameter("phoneNumber");
				firstName = request.getParameter("firstName");
				lastName = request.getParameter("lastName");

				Session session = sessionDAO.getSessionBySessionUUID((String) httpSession.getAttribute("user_session"));

				//TODO: the last param is to be the groupId and NOT 0.
				Event result = contactDAO.createContact(firstName, lastName, phoneNumber, email, session.getUserId(), 0);
				if(result == Event.ADD_CONTACT_SUCCESS) {
					Session sessionEntity = sessionDAO.getSessionBySessionUUID((String) httpSession.getAttribute("user_session"));
					//Contact was added successfully
				}
				else {
					//Failed to add contact to db.
				}
				if (true) {
					response.sendRedirect(request.getContextPath() + "/contacts");
					return;
				}
			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}			
			break;
		case "/addgroup":
			if(sessionAccepted) {
				String groupName = request.getParameter("groupName");

				Session session = sessionDAO.getSessionBySessionUUID((String) httpSession.getAttribute("user_session"));

				Event result = groupDAO.createGroup(groupName, session.getUserId());

				if (result != Event.CREATE_GROUP_SUCCESS) {
					//TODO: Handle this..
				}
				if (true) {
					response.sendRedirect(request.getContextPath() + "/groups");
					return;
				}

			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}
			break;
		case "/add-contact-to-group":
			if(sessionAccepted) {

				String groupId = request.getParameter("groupId");
				String contactId = request.getParameter("contactId");	

				Event addContactToGroupResponse = contactDAO.addContactToGroup(Integer.parseInt(groupId), Integer.parseInt(contactId));

				if(addContactToGroupResponse == Event.ADD_CONTACT_FAILED) {
					//TODO: Handle this failure...
				}

				if (true) {
					response.sendRedirect(request.getContextPath() + "/groups");
					return;
				}

			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}
			break;

		case "/sendmessage":

			if(sessionAccepted) {
				//groupId might be a list of contact_ids as well.. so check it :D
				String group_or_phonenumbers = request.getParameter("group_or_phonenumbers");
				String message = request.getParameter("message");
				int userId;
				String[] phoneNumbers = null;
				int groupId = 0;
				boolean isString = false;

				try {
					groupId = Integer.parseInt(group_or_phonenumbers);
				}
				catch(NumberFormatException e) {
					isString = true;						
				}

				Message messageEntity = new Message();

				Session session = sessionDAO.getSessionBySessionUUID((String) httpSession.getAttribute("user_session"));
				userId = session.getUserId();

				if(!isString) {
					ArrayList<Contact> contacts = contactDAO.getContactsByGroupId(groupId);
					phoneNumbers = new String[contacts.size()];

					for(int i = 0; i < contacts.size(); i++) {
						phoneNumbers[i] = contacts.get(i).getPhoneNumber();
					}
				}
				else {
					phoneNumbers = Translator.translateFromJsonToArray(group_or_phonenumbers);
					for(int i = 0; i < phoneNumbers.length; i++) {
						System.out.print("content is:  " + phoneNumbers[i]);
					}

				}
				messageEntity.setUserId(userId);
				messageEntity.setStatus(0); //0 means that message not registered
				messageEntity.setPhoneNumbers(phoneNumbers);
				messageEntity.setMessage(message);
				messageEntity.setCreationDate(getCurrentDate());

				Event createMessageResponse = messageDAO.createMessage(messageEntity);

				if(createMessageResponse != Event.CREATE_MESSAGE_SUCCESS) {
					//TODO: Handle this repsonse
				}

				if (true) {
					response.sendRedirect(request.getContextPath() + "/messages");
					return;
				}

			}
			else {
				dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/index.jsp");
			}				
			break;
		default:
			System.out.print("WRONG URI!!!!!");
			break;
		}
		
		if(!request.getServletPath().equals("/changemessagestatus")) {
			dispatcher.forward(request, response);
		}		
	}

	private String getCurrentDate() {
		String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		return date;
	}
}


