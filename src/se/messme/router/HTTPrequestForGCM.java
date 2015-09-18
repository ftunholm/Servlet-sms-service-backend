package se.messme.router;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import se.messme.DBconnector;
import se.messme.ExceptionLogger;
import se.messme.dao.DeviceDAO;
import se.messme.dao.MessageDAO;

public class HTTPrequestForGCM {
	
	private MessageDAO messageDAO;
	private DeviceDAO deviceDAO;

	public HTTPrequestForGCM() {
		messageDAO = new DBconnector();
		deviceDAO = new DBconnector();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new FixedRateTask(), 0, 10000);  
	}

	public String notifyAndroidUser(String regId) {
		HttpURLConnection connection = null;  

		try {
			//Create connection
			URL url = new URL("https://android.googleapis.com/gcm/send");
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Authorization", "key=AIzaSyCE6SHnR1hB3Ja6zzwvulaUMK3KZFwZWEI");
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");

			connection.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
			wr.writeBytes("registration_id=" + regId);
			wr.close();

			//Get Response  
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); 
			String line;
			while((line = rd.readLine()) != null) {
				response.append(line + "\n");
			}
			rd.close();
			return response.toString();
		} 
		catch (Exception e) {
			ExceptionLogger.log(e);		
			return null;
		} 
		finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}

	class FixedRateTask extends TimerTask {

		@Override
		public void run() {
			ArrayList<Integer> userIds = messageDAO.checkForNewMessage(); 
			
			if (userIds != null) {		
				for(int i = 0; i < userIds.size(); i++) {
					notifyAndroidUser(deviceDAO.getDevice((int) userIds.get(i)).getPushKey()); 
				}
			}
		}

	}
}