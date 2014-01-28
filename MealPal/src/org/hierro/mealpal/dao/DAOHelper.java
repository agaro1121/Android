package org.hierro.mealpal.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hierro.mealpal.dto.Entry;
import org.hierro.mealpal.dto.Type;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DAOHelper extends SQLiteOpenHelper {
	private static final String LOG = "DAOHelper";
	private static int DATABASE_VERSION = 1;
	private SimpleDateFormat dateFormatForDb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat dateFormatForPrinting = new SimpleDateFormat("yyyy-MM-dd");
	private Date date = new Date();

	//Database name
	private static final String DATABASE_NAME = "nutritionDiary";

	//Table Names
	private static final String TABLE_TYPES= "types";
	private static final String TABLE_ENTRIES = "entries";

	//Common column names
	private static final String KEY_ID = "id";
	private static final String KEY_TYPE = "type";

	//ENTRIES Table column names
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_AMOUNT_OF_LIQUID = "amount_of_liquid";
	private static final String KEY_TS = "ts";

	//ENTRIES Table Create Statement
	private static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS " +TABLE_ENTRIES+"( "+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+KEY_TYPE+" INTEGER, "+KEY_TS+" DATETIME, "
			+KEY_DESCRIPTION+" TEXT, "+KEY_AMOUNT_OF_LIQUID+" INTEGER"+")";
	private static final String CREATE_TABLE_TYPES = "CREATE TABLE IF NOT EXISTS "+TABLE_TYPES+"("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_TYPE+" TEXT)";


	public DAOHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		populateTypesTable();
		Log.d(LOG, "Exiting DAOHelper COnstructor");
	}

	public DAOHelper(Context context, int version) {
		super(context, DATABASE_NAME, null, version);
		DATABASE_VERSION = version;
		populateTypesTable();
		Log.d(LOG, "Exiting DAOHelper COnstructor");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(LOG, "About to create Tables");
		//creating required tables
		db.execSQL(CREATE_TABLE_ENTRIES);
		db.execSQL(CREATE_TABLE_TYPES);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPES);

		onCreate(db);

	}

	public long createType(Type type){
		Log.d(LOG, "Creating type");
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, type.getId());
		values.put(KEY_TYPE, type.getType());

		//insert row
		long type_id = db.insert(TABLE_TYPES, null, values);

		return type_id;
	}

	private List<Long> createTypes(){
		Log.d(LOG, "Creating type");
		List<Long> returnValues = new ArrayList<Long>();
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, 1);
		values.put(KEY_TYPE, "water");
		long type_id = db.insert(TABLE_TYPES, null, values);
		returnValues.add(type_id);

		values = new ContentValues();
		values.put(KEY_ID, 2);
		values.put(KEY_TYPE, "other");
		long type_id2 = db.insert(TABLE_TYPES, null, values);
		returnValues.add(type_id2);

		values = new ContentValues();
		values.put(KEY_ID, 3);
		values.put(KEY_TYPE, "meal");
		long type_id3 = db.insert(TABLE_TYPES, null, values);
		returnValues.add(type_id3);

		values = new ContentValues();
		values.put(KEY_ID, 4);
		values.put(KEY_TYPE, "snack");
		long type_id4 = db.insert(TABLE_TYPES, null, values);
		returnValues.add(type_id4);

		db.close();
		return returnValues;
	}

	public void populateTypesTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(CREATE_TABLE_TYPES);
		String query = "SELECT * FROM "+TABLE_TYPES;
		Cursor cursor = db.rawQuery(query, null);

		if(cursor.getCount() < 4){
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPES);
			db.execSQL(CREATE_TABLE_TYPES);
			createTypes();
			return;
		}

		cursor.close();
		db.close();
		return;
	}

	public long createFoodEntry(Entry entry){
		Log.d(LOG, "Creating Food Entry");
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, entry.getType());
		values.put(KEY_TS, entry.getTsAsString());
		values.put(KEY_DESCRIPTION, entry.getDescription());
		values.put(KEY_AMOUNT_OF_LIQUID, 0);

		//insert row
		long type_id = db.insert(TABLE_ENTRIES, null, values);

		db.close();
		return type_id;
	}

	public long createLiquidEntry(Entry LiquidEntry){
		Log.d(LOG, "Creating Liquid Entry");
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, LiquidEntry.getType());
		values.put(KEY_TS, LiquidEntry.getTsAsString());
		values.put(KEY_DESCRIPTION, LiquidEntry.getDescription());
		values.put(KEY_AMOUNT_OF_LIQUID, LiquidEntry.getAmount());

		//insert row
		long type_id = db.insert(TABLE_ENTRIES, null, values);

		db.close();
		return type_id;
	}

	public List<Entry> fetchDayLog(String selectedDate) {
		SQLiteDatabase db = this.getWritableDatabase();
		List<Entry> todayListings = new ArrayList<Entry>();
//		String selectedDate =  dateFormatForPrinting.format(date); 
		String query = "select strftime('%Y-%m-%d %H:%M:%S', ts) as ts,type,description,amount_of_liquid from entries " +
						"where ts like '%"+selectedDate+"%' order by ts";
		Cursor cursor = db.rawQuery(query, null);
		Entry currentEntry = null;
		
		if(cursor.moveToFirst()){
			do {
				try {
					currentEntry = new Entry(cursor.getInt(1), 
											 dateFormatForDb.parse( (cursor.getString(0)) ),//TODO needs testing 
											 cursor.getInt(3), 
											 cursor.getString(2));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				todayListings.add(currentEntry);
			} while(cursor.moveToNext());
		}
		return todayListings;
	}

	public List<Entry> fetchDayLog(String selectedDate, int type, int type2) {
		SQLiteDatabase db = this.getWritableDatabase();
		List<Entry> todayListings = new ArrayList<Entry>();
//		String selectedDate =  dateFormatForPrinting.format(date); 
		String query = "select strftime('%Y-%m-%d %H:%M:%S', ts) as ts,type,description,amount_of_liquid from entries " +
				"where ts like '%"+selectedDate+"%' and (type = "+type+" or type = "+type2+") order by ts";
		Log.d(LOG, query);
		Cursor cursor = db.rawQuery(query, null);
		Entry currentEntry = null;
		
		if(cursor.moveToFirst()){
			do {
				try {
					currentEntry = new Entry(cursor.getInt(1), 
											 dateFormatForDb.parse( (cursor.getString(0)) ),//TODO needs testing 
											 cursor.getInt(3), 
											 cursor.getString(2));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				todayListings.add(currentEntry);
			} while(cursor.moveToNext());
		}
		return todayListings;
	}
	
}
