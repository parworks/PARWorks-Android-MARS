package com.parworks.mars.model.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.parworks.mars.model.databasetables.DatabaseHelper;
import com.parworks.mars.model.databasetables.TrendingSitesTable;

public class TrendingSitesContentProvider extends ContentProvider {

	private static final String TAG = "TrendingSitesContentProvider";

	private DatabaseHelper dbHelper;

	public static final String AUTHORITY = "com.parworks.mars.provider.trending";	
	private static final String BASE_PATH = "trending";

	private static final int TRENDING_SITES = 30;

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "trendingsites";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/trendingsites";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH) {{
		addURI(AUTHORITY, BASE_PATH, TRENDING_SITES);
	}};

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "Insert record for TrendingSites");

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case TRENDING_SITES:
			id = db.insert(TrendingSitesTable.TABLE_NAME, null, values);
			Log.d(TAG,"The id after inserting was: " + id);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);			
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);	
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DatabaseHelper(this.getContext());
		System.out.println("YUSUN INIT");
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, 
			String[] selectionArgs,	String sortOrder) {
		Log.d(TAG, "Query trending sites content provider: " + selection);		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();		
		queryBuilder.setTables(TrendingSitesTable.TABLE_NAME);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {		
		case TRENDING_SITES:
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
}
