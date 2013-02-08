package com.parworks.mars.model.provider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.parworks.mars.model.db.DatabaseHelper;
import com.parworks.mars.model.db.SiteInfoTable;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MarsContentProvider extends ContentProvider {
	
	private DatabaseHelper database;
	
	private static final int SITES = 10;
	private static final int SITES_ID = 20;
	
	public static final String AUTHORITY = "com.parworks.mars.provider";
	
	private static final String BASE_PATH = "sites";
	public static final Uri ALL_SITES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "sites";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/sites";
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	public static final String TAG = MarsContentProvider.class.getName();
	
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, SITES);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/*", SITES_ID);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
	    int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    int rowsDeleted = 0;
	    switch (uriType) {
	    case SITES:
	      rowsDeleted = sqlDB.delete(SiteInfoTable.TABLE_SITES, selection,
	          selectionArgs);
	      break;
	    case SITES_ID:
	      String id = uri.getLastPathSegment();
	      if (TextUtils.isEmpty(selection)) {
	        rowsDeleted = sqlDB.delete(SiteInfoTable.TABLE_SITES,
	        		SiteInfoTable.COLUMN_SITE_ID + "=" + id, 
	            null);
	      } else {
	        rowsDeleted = sqlDB.delete(SiteInfoTable.TABLE_SITES,
	        		SiteInfoTable.COLUMN_SITE_ID + "=" + id 
	            + " and " + selection,
	            selectionArgs);
	      }
	      break;
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG,"Insert in contentProvider");
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case SITES:
			Log.d(TAG,"CASE OVERLAYS");
			try {
				id = sqlDB.insertOrThrow(SiteInfoTable.TABLE_SITES, null, values);
				Log.d(TAG,"Inserted site: " + values.getAsString("siteId"));
			} catch(SQLiteConstraintException exception) {
				Log.d(TAG, "SQLiteConstraintException: " + exception.getMessage());
			}
			
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
			
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		database = new DatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		checkColumns(projection);
		queryBuilder.setTables(SiteInfoTable.TABLE_SITES);
		  int uriType = sURIMatcher.match(uri);
		    switch (uriType) {
		    case SITES:
		      Log.d(TAG, "Query was SITES: this is unimplemented");
		      break;
		    case SITES_ID:
		    	Log.d(TAG, "Query was SITES_ID with id: " + uri.getLastPathSegment());
		      // Adding the ID to the original query
		      queryBuilder.appendWhere(SiteInfoTable.COLUMN_SITE_ID + "="
		          + "'"+ uri.getLastPathSegment() +"'");
		      break;
		    default:
		      throw new IllegalArgumentException("Unknown URI: " + uri);
		    }

		    SQLiteDatabase db = database.getWritableDatabase();
		    Cursor cursor = queryBuilder.query(db, projection, selection,
		        selectionArgs, null, null, sortOrder);
		    // Make sure that potential listeners are getting notified
		    cursor.setNotificationUri(getContext().getContentResolver(), uri);
		    cursor.moveToFirst();
		    return cursor;
	}
	
	public static Uri getSiteUri(String siteId) {
		return Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH + "/" + siteId);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


	    	    int uriType = sURIMatcher.match(uri);
	    	    SQLiteDatabase sqlDB = database.getWritableDatabase();
	    	    int rowsUpdated = 0;
	    	    switch (uriType) {
	    	    case SITES:
	    	      rowsUpdated = sqlDB.update(SiteInfoTable.TABLE_SITES, 
	    	          values, 
	    	          selection,
	    	          selectionArgs);
	    	      break;
	    	    case SITES_ID:
	    	      String id = uri.getLastPathSegment();
	    	      if (TextUtils.isEmpty(selection)) {
	    	        rowsUpdated = sqlDB.update(SiteInfoTable.TABLE_SITES, 
	    	            values,
	    	            SiteInfoTable.COLUMN_SITE_ID + "=" + id, 
	    	            null);
	    	      } else {
	    	        rowsUpdated = sqlDB.update(SiteInfoTable.TABLE_SITES, 
	    	            values,
	    	            SiteInfoTable.COLUMN_SITE_ID + "=" + id 
	    	            + " and " 
	    	            + selection,
	    	            selectionArgs);
	    	      }
	    	      break;
	    	    default:
	    	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    	    }
	    	    getContext().getContentResolver().notifyChange(uri, null);
	    	    return rowsUpdated;
	}
	
	private void checkColumns(String[] projection) {
		String[] available = SiteInfoTable.ALL_COLUMNS;
		if(projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			if(!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
}
