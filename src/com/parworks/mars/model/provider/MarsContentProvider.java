package com.parworks.mars.model.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.parworks.mars.model.db.AugmentedImagesTable;
import com.parworks.mars.model.db.DatabaseHelper;
import com.parworks.mars.model.db.SiteInfoTable;
import com.parworks.mars.model.db.TrendingSitesTable;

/**
 * Content provider for the whole MARS app.
 * 
 * @author yusun
 */
public class MarsContentProvider extends ContentProvider {
	
	public static final String TAG = "MarsContentProvider";
	
	/** Access the database here */
	private DatabaseHelper dbHelper;

	/** Mars app exposes a single content provider authority */
	public static final String AUTHORITY = "com.parworks.mars.provider";
	
	/** Constants used for URI matcher */
	private static final int SITES = 10;
	private static final int SITE_ID = 12;
	private static final int TRENDING_SITES = 20;
	private static final int AUGMENTED_IMAGES = 30;
	private static final int AUGMENTED_IMAGE_ID = 32;
	private static final int AUGMENTED_SITE_ID = 32;

	/** Associated with SiteInfoTable */
	private static final String BASE_PATH_SITE = "site";
	/** Associated with TrendingSitesTable */
	private static final String BASE_PATH_TRENDING_SITE = "trending";
	/** Associated with AugmentedImagesTable */
	private static final String BASE_PATH_AUGMENTED_IMAGE = "augment";
	
	/** Construct the URI matcher for all content */
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH) {{
		// SiteInfo
		addURI(AUTHORITY, BASE_PATH_SITE, SITES);
		addURI(AUTHORITY, BASE_PATH_SITE + "/*", SITE_ID);
		// TrendingSites
		addURI(AUTHORITY, BASE_PATH_TRENDING_SITE, TRENDING_SITES);
		// AugmentedImages
		addURI(AUTHORITY, BASE_PATH_AUGMENTED_IMAGE, AUGMENTED_IMAGES);
		addURI(AUTHORITY, BASE_PATH_AUGMENTED_IMAGE +"/*", AUGMENTED_IMAGE_ID);
		addURI(AUTHORITY, BASE_PATH_AUGMENTED_IMAGE +"/site/*", AUGMENTED_SITE_ID);
	}};

	/** Helper URIs for the callers to use */
	public static final Uri CONTENT_URI_ALL_SITES = 
			Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_SITE);
	public static final Uri CONTENT_URI_ALL_TRENDING_SITES = 
			Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_TRENDING_SITE);
	public static final Uri CONTENT_URI_ALL_AUGMENTED_IMAGES = 
			Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_AUGMENTED_IMAGE);
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {		
		case SITES:    // delete the whole site table
			rowsDeleted = db.delete(SiteInfoTable.TABLE_SITES, selection, selectionArgs);
			break;
		case SITE_ID:  // delete a certain site info record 
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(SiteInfoTable.TABLE_SITES,
						SiteInfoTable.COLUMN_SITE_ID + "=" + "'" + id + "'", 
						null);
			} else {
				rowsDeleted = db.delete(SiteInfoTable.TABLE_SITES,
						SiteInfoTable.COLUMN_SITE_ID + "=" + "'" + id + "'" 
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case TRENDING_SITES: // delete the whole trending site table
			rowsDeleted = db.delete(TrendingSitesTable.TABLE_NAME, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long id = 0;
		Uri returnedUri = null;
		switch (uriType) {
		case SITES:    // insert a new SiteInfo record
			try {
				id = db.insertOrThrow(SiteInfoTable.TABLE_SITES, null, values);
				Log.d(TAG,"Inserted site: " + values.getAsString("siteId"));
				returnedUri = Uri.parse(BASE_PATH_SITE + "/" + id);
			} catch(SQLiteConstraintException exception) {
				Log.d(TAG, "SQLiteConstraintException: " + exception.getMessage());
			}
			break;
		case TRENDING_SITES: // insert a new TrendingSite record
			try {
				id = db.insertOrThrow(TrendingSitesTable.TABLE_NAME, null, values);
				Log.d(TAG,"Inserted trending site: " + values.getAsString("siteId"));
				returnedUri = Uri.parse(BASE_PATH_TRENDING_SITE + "/" + id);
			} catch(SQLiteConstraintException exception) {
				Log.d(TAG, "SQLiteConstraintException: " + exception.getMessage());
			}
			break;
		case AUGMENTED_IMAGES: // insert a new AugmentedImage record
			try {
				id = db.insertOrThrow(AugmentedImagesTable.TABLE_NAME, null, values);
				Log.d(TAG,"Inserted augmented image for site: " + values.getAsString("siteId"));
				returnedUri = Uri.parse(BASE_PATH_AUGMENTED_IMAGE + "/" + id);
			} catch(SQLiteConstraintException exception) {
				Log.d(TAG, "SQLiteConstraintException: " + exception.getMessage());
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return returnedUri;
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		int uriType = sURIMatcher.match(uri);
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		switch (uriType) {
		case SITES:
			Log.d(TAG, "Query was SITES: this is unimplemented");
			break;
		case SITE_ID:  // query a SiteInfo record
			Log.d(TAG, "Query was SITES_ID with id: " + uri.getLastPathSegment());
			queryBuilder.setTables(SiteInfoTable.TABLE_SITES);
			queryBuilder.appendWhere(SiteInfoTable.COLUMN_SITE_ID + "="
					+ "'" + uri.getLastPathSegment() + "'");
			break;
		case TRENDING_SITES: // query all the trending sites
			queryBuilder.setTables(TrendingSitesTable.TABLE_NAME);
			break;
		case AUGMENTED_SITE_ID: // query all the augmented images for a given site
			queryBuilder.setTables(AugmentedImagesTable.TABLE_NAME);
			queryBuilder.appendWhere(AugmentedImagesTable.COLUMN_SITE_ID + "="
					+ "'" + uri.getLastPathSegment() + "'");
			sortOrder = AugmentedImagesTable.COLUMN_TIMESTAMP + " DES";
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		cursor.moveToFirst();
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case SITES:    // update the whole SiteInfo table
			rowsUpdated = sqlDB.update(SiteInfoTable.TABLE_SITES, 
					values, 
					selection,
					selectionArgs);
			break;
		case SITE_ID:  // update a SiteInfo record
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(SiteInfoTable.TABLE_SITES, 
						values,
						SiteInfoTable.COLUMN_SITE_ID + "=" + "'" + id + "'", 
						null);
			} else {
				rowsUpdated = sqlDB.update(SiteInfoTable.TABLE_SITES, 
						values,
						SiteInfoTable.COLUMN_SITE_ID + "=" + "'" + id + "'" 
						+ " and " + selection,
						selectionArgs);
			}
			break;
		case AUGMENTED_IMAGE_ID:  // update an AugmentedImage record
			rowsUpdated = sqlDB.update(AugmentedImagesTable.TABLE_NAME, 
					values,
					AugmentedImagesTable.COLUMN_IMAGE_ID + "=" + "'" + uri.getLastPathSegment() + "'", 
					null);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DatabaseHelper(getContext());
		return true;
	}

	public static Uri getSiteUri(String siteId) {
		return Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_SITE + "/" + siteId);
	}
	
	public static Uri getSiteAugmentedImagesUri(String siteId) {
		return Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_AUGMENTED_IMAGE + "/site/" + siteId);
	}
	
	public static Uri getAugmentedImageUri(String imgId) {
		return Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_AUGMENTED_IMAGE + "/" + imgId);
	}
	
	@Override
	public String getType(Uri uri) {
		return null;
	}	
}