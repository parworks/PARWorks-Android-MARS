package com.parworks.mars.model.providers;

import android.content.ContentProvider;

import com.parworks.mars.model.databasetables.DatabaseHelper;

public abstract class AbstractMarsContentProvider extends ContentProvider {
	
	protected DatabaseHelper dbHelper;
	
	/** Content provider authority for MARS app */
	public static final String AUTHORITY = "com.parworks.mars.contentprovider";
	
	@Override
	public boolean onCreate() {
		dbHelper = new DatabaseHelper(this.getContext());
		return true;
	}
}