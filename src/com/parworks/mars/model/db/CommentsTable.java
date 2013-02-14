package com.parworks.mars.model.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CommentsTable {
	/** Table Name */
	public static final String TABLE_NAME = "Comments";

	/** Table Columns */
	private static final String COLUMN_INDEX = "_id";
	public static final String COLUMN_SITE_ID = "siteId";
	public static final String COLUMN_USER_ID = "userId";
	public static final String COLUMN_TIMESTAMP = "time";
	public static final String COLUMN_COMMENT = "comment";
	public static final String COLUMN_USER_NAME = "username";

	public static final String ALL_COLUMNS[] = { 
		COLUMN_INDEX,
		COLUMN_SITE_ID,  COLUMN_USER_ID, COLUMN_COMMENT, COLUMN_USER_NAME,		   
		COLUMN_TIMESTAMP
	};

	/** Database Creation SQL Statement */
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME
			+ "("
			+ COLUMN_INDEX + " integer PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_SITE_ID + " text NOT NULL,"
			+ COLUMN_TIMESTAMP + " integer,"
			+ COLUMN_USER_ID + " text NOT NULL,"
			+ COLUMN_COMMENT + " text NOT NULL,"
			+ COLUMN_USER_NAME + " text,"
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
