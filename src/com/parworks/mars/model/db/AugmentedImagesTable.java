package com.parworks.mars.model.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AugmentedImagesTable {

	/** Table Name */
	public static final String TABLE_NAME = "AugmentedImages";

	/** Table Columns */
	private static final String COLUMN_INDEX = "_id";
	public static final String COLUMN_SITE_ID = "siteId";
	public static final String COLUMN_IMAGE_ID = "imgId";
	public static final String COLUMN_USER_ID = "userId";
	public static final String COLUMN_WIDTH = "width";
	public static final String COLUMN_HIEGHT = "height";
	public static final String COLUMN_FULL_SIZE_URL = "fullSizeUrl";
	public static final String COLUMN_GALLERY_SIZE_URL = "galleerySizeUrl";
	public static final String COLUMN_CONTENT_SIZE_URL = "contentSizeUrl";
	public static final String COLUMN_TIMESTAMP = "time";
	public static final String COLUMN_CONTENT = "content";

	public static final String ALL_COLUMNS[] = { 
		COLUMN_INDEX,
		COLUMN_SITE_ID, COLUMN_IMAGE_ID, COLUMN_USER_ID, COLUMN_WIDTH,
		COLUMN_HIEGHT, COLUMN_FULL_SIZE_URL, COLUMN_GALLERY_SIZE_URL, COLUMN_CONTENT_SIZE_URL,
		COLUMN_TIMESTAMP, COLUMN_CONTENT
	};

	/** Database Creation SQL Statement */
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME
			+ "("
			+ COLUMN_INDEX + " integer PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_SITE_ID + " text NOT NULL, "
			+ COLUMN_IMAGE_ID + " text NOT NULL, "
			+ COLUMN_USER_ID + " text, " 
			+ COLUMN_WIDTH + " integer, "  
			+ COLUMN_HIEGHT + " integer, " 
			+ COLUMN_FULL_SIZE_URL + " text," 
			+ COLUMN_GALLERY_SIZE_URL + " text,"
			+ COLUMN_CONTENT_SIZE_URL + " text,"
			+ COLUMN_CONTENT + " text,"
			+ COLUMN_TIMESTAMP + " integer,"
			+ "FOREIGN KEY (" + COLUMN_SITE_ID + ") REFERENCES " + SiteInfoTable.TABLE_SITES + "(" + SiteInfoTable.COLUMN_SITE_ID +") "
			+ ");";

	protected static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	protected static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(SiteInfoTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}
}
