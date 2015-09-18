package se.messme;

import com.google.gson.Gson;

public class Translator {
	
	public static String translateToJson(Object obj) {
		Gson gson = new Gson();
		
		String result = gson.toJson(obj);
		
		return result;
	}
	
	public static Object translateFromJson(String json, Class<?> clazz) {
		Gson gson = new Gson();
		Object obj = gson.fromJson(json, clazz);
		return obj;		
	}
	
	public static String[] translateFromJsonToArray(String json) {
		Gson gson = new Gson();
		String[] array = gson.fromJson(json, String[].class);
		return array;	
	}

}
