package se.messme;

import java.util.Timer;
import java.util.TimerTask;

import se.messme.dao.SessionDAO;

public class ClearSessionCron {
	private SessionDAO sessionDAO;
	
	public ClearSessionCron() {
		sessionDAO = new DBconnector();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new FixedRateTask(), 0, 60000*60*24);  
	}
	
	class FixedRateTask extends TimerTask {

		@Override
		public void run() {
			Event removeSessionResponse = sessionDAO.removeSessionByTime();
			
			if(removeSessionResponse != null) {
				if(removeSessionResponse != Event.REMOVE_SESSION_SUCCESS) {
					//TODO handle this ...
				}
			}
		}

	}

}

