package com.parworks.mars.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "parworks.mars.db";
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		SiteInfoTable.onCreate(database);		
		TrendingSitesTable.onCreate(database);
		AugmentedImagesTable.onCreate(database);
		CommentsTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		SiteInfoTable.onUpgrade(database, oldVersion, newVersion);		
		TrendingSitesTable.onUpgrade(database, oldVersion, newVersion);
		AugmentedImagesTable.onUpgrade(database, oldVersion, newVersion);
		CommentsTable.onUpgrade(database, oldVersion, newVersion);
	}
}
