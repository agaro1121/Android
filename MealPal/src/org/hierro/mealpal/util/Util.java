package org.hierro.mealpal.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.R;

public final class Util {

	public static int getType(String type){
		int typeInt = -1;
		
		if(type.equalsIgnoreCase("water")){
			typeInt = 1;
		}
		if(type.equalsIgnoreCase("other")){
			typeInt = 2;
		}
		if(type.equalsIgnoreCase("meal")){
			typeInt = 3;
		}
		if(type.equalsIgnoreCase("snack")){
			typeInt = 4;
		}
		return typeInt;
	}
	
	public static int getAmount(String measurement){
		int amount=-1;
		
		if(measurement.equalsIgnoreCase("cup")){
			amount = 8;
		}
		if(measurement.equalsIgnoreCase("pint")){
			amount = 16;
		}
		if(measurement.equalsIgnoreCase("quart")){
			amount = 32;
		}
		return amount;
	}
	
	public static boolean isEmpty(String input){
		if(input == null){
			return true;
		}
		if(input.equalsIgnoreCase("")){
			return true; 
		}
		
		return false;
	}
	
	public static String getTypeString(int input){
		switch(input){
		case 1:
			return "water";
		case 2:
			return "Non-H2O";
		case 3:
			return "meal";
		case 4:
			return "snack";
		}
		
		return "error";
	}
	
	public static int getHours(int hours){
		int convertedHours = -1;
		
		if(hours > 12){
			convertedHours = hours - 12;
		}else if(hours == 0){
			convertedHours = 12;
		}else if(hours == 12){
			convertedHours = 12;
		}else{
			convertedHours = hours;
		}
		
		return convertedHours;
	}
	
	public static String getAmPm(int hours){
		String ampm = "";
		
		if(hours >= 12){
			ampm = " PM  ";
		}else if(hours == 0){
			ampm = " AM  ";
		}else{
			ampm = " AM  ";
		}
		
		return ampm;
	}
	
	public static String getMins(Date date){
		SimpleDateFormat minFormat = new SimpleDateFormat("mm");
		
		return minFormat.format(date);
	}
}
