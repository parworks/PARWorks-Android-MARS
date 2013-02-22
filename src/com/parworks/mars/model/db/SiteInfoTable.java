package com.parworks.mars.model.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class SiteInfoTable {

	//table name
	public static final String TABLE_SITES = "sites";
	
	//columns
	private static final String COLUMN_INDEX = "_id";
	public static final String COLUMN_SITE_ID = "siteId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_STATE = "state";
	public static final String COLUMN_DESC = "desc";
	public static final String COLUMN_CHANNEL = "channel";
	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LON = "lon";
	public static final String COLUMN_PROFILE = "profile";
	public static final String COLUMN_FEATURE_DESC = "featdesc";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_POSTER_IMAGE_URL = "posterimageurl";
	public static final String COLUMN_POSTER_IMAGE_CONTENT = "posterimagecontent";
	public static final String COLUMN_AUG_POSTER_IMAGE_URL = "augposterimageurl";
	public static final String COLUMN_AUG_POSTER_BLURRED_IMAGE_URL = "augposterblurimageurl";
	public static final String COLUMN_AUG_POSTER_IMAGE_CONTENT = "augposterimagecontent";
	public static final String COLUMN_AUG_POSTER_IMAGE_WIDTH = "augposterimagewidth";
	public static final String COLUMN_AUG_POSTER_IMAGE_HEIGHT = "augposterimageheight";
	public static final String COLUMN_TAG_LIST = "taglist";
	
	public static final String ALL_COLUMNS[] = { COLUMN_INDEX, COLUMN_SITE_ID, COLUMN_NAME, COLUMN_STATE,COLUMN_DESC,COLUMN_CHANNEL,
		COLUMN_LAT,COLUMN_LON,COLUMN_PROFILE,COLUMN_FEATURE_DESC,COLUMN_ADDRESS,COLUMN_POSTER_IMAGE_URL,
		COLUMN_POSTER_IMAGE_CONTENT, COLUMN_AUG_POSTER_BLURRED_IMAGE_URL, 
		COLUMN_AUG_POSTER_IMAGE_URL,
		COLUMN_AUG_POSTER_IMAGE_CONTENT, COLUMN_AUG_POSTER_IMAGE_WIDTH,
		COLUMN_AUG_POSTER_IMAGE_HEIGHT, COLUMN_TAG_LIST };
	
	//database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_SITES
			+ "("
			+ COLUMN_INDEX + " integer PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_SITE_ID + " text UNIQUE NOT NULL, "
			+ COLUMN_NAME + " text, "
			+ COLUMN_STATE + " text, " 
			+ COLUMN_DESC + " text, " 
			+ COLUMN_CHANNEL + " text, " 
			+ COLUMN_LAT + " text, " 
			+ COLUMN_LON + " text," 
			+ COLUMN_FEATURE_DESC + " text,"
			+ COLUMN_PROFILE + " text,"
			+ COLUMN_ADDRESS + " text,"
			+ COLUMN_POSTER_IMAGE_URL + " text,"
			+ COLUMN_POSTER_IMAGE_CONTENT + " text,"
			+ COLUMN_AUG_POSTER_IMAGE_URL + " text,"
			+ COLUMN_AUG_POSTER_BLURRED_IMAGE_URL + " text,"
			+ COLUMN_AUG_POSTER_IMAGE_CONTENT + " text,"
			+ COLUMN_AUG_POSTER_IMAGE_WIDTH + " integer,"
			+ COLUMN_AUG_POSTER_IMAGE_HEIGHT + " integer,"
			+ COLUMN_TAG_LIST + " text"
			+ ");";
	
	protected static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	protected static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	    Log.w(SiteInfoTable.class.getName(), "Upgrading database from version "
	            + oldVersion + " to " + newVersion
	            + ", which will destroy all old data");
	        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SITES);
	        onCreate(database);
	}
}