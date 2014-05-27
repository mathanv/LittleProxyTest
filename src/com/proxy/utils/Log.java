package com.proxy.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {

	private String message;
	private final static String TAG = "Saiku Test Automation";
	
	public static void d(String tag, String message){
		System.out.println(getCurrentDate() + "\t" + TAG + ":" + tag + "\t" +  message);
	}
	
	public static String getCurrentDate ()
    {
            String currentDate = "";
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            currentDate = simpleFormat.format(Calendar.getInstance().getTime());
            return currentDate;
    }

}
