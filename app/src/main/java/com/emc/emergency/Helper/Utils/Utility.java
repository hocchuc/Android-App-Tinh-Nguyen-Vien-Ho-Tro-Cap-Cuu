package com.emc.emergency.Helper.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Class which has Utility methods
 * 
 */
public class Utility {
	private static Pattern pattern;
	private static Matcher matcher;
	//Email Pattern
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	/**
	 * Validate Email with regular expression
	 * 
	 * @param email
	 * @return true for Valid Email and false for Invalid Email
	 */
	public static boolean validate(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
 
	}
	/**
	 * Checks for Null String object
	 * 
	 * @param txt
	 * @return true for not null and false for null String object
	 */
	public static boolean isNotNull(String txt){
		return txt!=null && txt.trim().length()>0 ? true: false;
	}

	public static void dumpIntent(Intent i, String LOG_TAG){

	    Bundle bundle = i.getExtras();
	    if (bundle != null) {
	        Set<String> keys = bundle.keySet();
	        Iterator<String> it = keys.iterator();
	        Log.e(LOG_TAG,"Dumping Intent start");
	        while (it.hasNext()) {
	            String key = it.next();
	            Log.e(LOG_TAG,"[" + key + "=" + bundle.get(key)+"]");
	        }
	        Log.e(LOG_TAG,"Dumping Intent end");
	    }
	}
}
