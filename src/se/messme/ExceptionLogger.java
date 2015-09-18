package se.messme;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExceptionLogger {

	public static void log(Exception e) {
		try {

			FileWriter log = new FileWriter("error_log.txt", true);
			
			//create an print writer for writing to a file
			PrintWriter out = new PrintWriter(log);

			out.println(getCurrentDate());
			e.printStackTrace(out);
			out.println("");
			
			//close the file 
			out.close();
		}
		catch(IOException e1) {			
			System.out.println("Error during reading/writing");
		}
	}
	
	private static String getCurrentDate() {
		String date = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
		return date;
	}

}
