package com.parworks.mars.model.provider;

import android.content.ContentProvider;

import com.parworks.mars.model.db.DatabaseHelper;

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