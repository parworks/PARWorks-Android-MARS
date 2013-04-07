/*******************************************************************************
 * Copyright 2013 PAR Works, Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.parworks.mars.model.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TrendingSitesTable {

	/** Table Name */
	public static final String TABLE_NAME = "TrendingSites";

	/** Table Columns */
	private static final String COLUMN_INDEX = "_id";
	public static final String COLUMN_SITE_ID = "siteId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_STATE = "state";
	public static final String COLUMN_DESC = "desc";
	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LON = "lon";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_POSTER_IMAGE_URL = "posterimageurl";
	public static final String COLUMN_POSTER_BLURRED_IMAGE_URL = "posterblurimageurl";
	public static final String COLUMN_POSTER_IMAGE_CONTENT = "posterimagecontent";
	public static final String COLUMN_AUG_POSTER_IMAGE_URL = "augposterimageurl";
	public static final String COLUMN_AUG_POSTER_BLURRED_IMAGE_URL = "augposterblurimageurl";
	public static final String COLUMN_AUG_POSTER_IMAGE_CONTENT = "augposterimagecontent";
	public static final String COLUMN_AUG_POSTER_IMAGE_WIDTH = "augposterimagewidth";
	public static final String COLUMN_AUG_POSTER_IMAGE_HEIGHT = "augposterimageheight";
	public static final String COLUMN_NUM_AUGMENTED_IMAGES = "numaugmentedimages";

	public static final String ALL_COLUMNS[] = { 
		COLUMN_INDEX,
		COLUMN_SITE_ID, COLUMN_NAME, COLUMN_STATE, COLUMN_DESC,
		COLUMN_LAT, COLUMN_LON, COLUMN_ADDRESS, COLUMN_POSTER_IMAGE_URL,
		COLUMN_POSTER_IMAGE_CONTENT, COLUMN_POSTER_BLURRED_IMAGE_URL,
		COLUMN_AUG_POSTER_IMAGE_URL, COLUMN_AUG_POSTER_BLURRED_IMAGE_URL, 
		COLUMN_AUG_POSTER_IMAGE_CONTENT, COLUMN_AUG_POSTER_IMAGE_WIDTH,
		COLUMN_AUG_POSTER_IMAGE_HEIGHT, COLUMN_NUM_AUGMENTED_IMAGES
	};	

	/** Database Creation SQL Statement */
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME
			+ "("
			+ COLUMN_INDEX + " integer PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_SITE_ID + " text NOT NULL, "
			+ COLUMN_NAME + " text, "
			+ COLUMN_STATE + " text, " 
			+ COLUMN_DESC + " text, "  
			+ COLUMN_LAT + " text, " 
			+ COLUMN_LON + " text," 
			+ COLUMN_ADDRESS + " text,"
			+ COLUMN_POSTER_IMAGE_URL + " text,"
			+ COLUMN_POSTER_BLURRED_IMAGE_URL + " text,"
			+ COLUMN_POSTER_IMAGE_CONTENT + " text,"			
			+ COLUMN_AUG_POSTER_IMAGE_URL + " text,"
			+ COLUMN_AUG_POSTER_BLURRED_IMAGE_URL + " text,"
			+ COLUMN_AUG_POSTER_IMAGE_CONTENT + " text,"
			+ COLUMN_AUG_POSTER_IMAGE_WIDTH + " integer,"
			+ COLUMN_AUG_POSTER_IMAGE_HEIGHT + " integer,"
			+ COLUMN_NUM_AUGMENTED_IMAGES + " integer"
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
