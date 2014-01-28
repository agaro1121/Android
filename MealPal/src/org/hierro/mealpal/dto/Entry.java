package org.hierro.mealpal.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.hierro.mealpal.util.Util;



public class Entry {
	private long id;
	private int type;
	private Date ts;
	private String description;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private int amount;

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getId() {
		return id;
	}
	public Date getTs() {
		return ts;
	}

	public int getAmount() {
		return amount;
	}
	private Date convertStringToDate(String s){
		Date ts = null;
		if(s == null){
			ts = new Date();
		}else{
			try {
				ts = formatter.parse(s);
			} catch (ParseException e) {
				//TODO Log this
			}
		}
		return ts;
	}

	@Override
	public String toString() {
		String returnString = "";

		if(type == 1 || type == 2){//liquids
			returnString = Util.getHours(ts.getHours())+":"+Util.getMins(ts)+" "
					+Util.getAmPm(ts.getHours())+" "+Util.getTypeString(type)+" "
					+description+" "+amount+"oz";
		}else{
			returnString = Util.getHours(ts.getHours())+":"+Util.getMins(ts)+" "+Util.getAmPm(ts.getHours())+" "+Util.getTypeString(type)+" "+description;
		}

		return returnString;
	}
	
	public String getTsAsString(){
		return formatter.format(ts);
	}

	public Entry(int type, Date ts, String description) {
		super();
		this.type = type;
		this.ts = ts;
		this.description = description;
	}

	public Entry(int type,Date ts,int amount,String  description){
		this.type = type;
		this.ts = ts;
		this.amount = amount;
		this.description = description;
	}
}
