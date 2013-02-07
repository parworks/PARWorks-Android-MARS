package com.parworks.mars;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.parworks.mars.account.MarsAccountHelper;
import com.parworks.mars.model.databasetables.TrendingSitesTable;
import com.parworks.mars.model.providers.TrendingSitesContentProvider;

public class Test extends FragmentActivity implements
LoaderManager.LoaderCallbacks<Cursor> {

	// private Cursor cursor;
	private SimpleCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		Button b1 = (Button) findViewById(R.id.buttonInsert);
		b1.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				MarsAccountHelper.sync("OptioLabs");
//				ContentValues cv = new ContentValues();
//				cv.put(TrendingSitesTable.COLUMN_SITE_ID, UUID.randomUUID().toString());
//				cv.put(TrendingSitesTable.COLUMN_DESC, "test");
//
//				getContentResolver().insert(TrendingSitesContentProvider.CONTENT_URI, cv);
			}
		});

		Button b2 = (Button) findViewById(R.id.buttonPrint);
		b2.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				String[] projection = TrendingSitesTable.ALL_COLUMNS;
				Cursor cursor = getContentResolver().query(TrendingSitesContentProvider.CONTENT_URI, 
						projection, null, null,	null);

				if (cursor != null) {
					cursor.moveToFirst();
					while (!cursor.isAfterLast()) {
						System.out.println("TESTCUROSR: " + cursor.getString(
								cursor.getColumnIndex(TrendingSitesTable.COLUMN_SITE_ID)));
						cursor.moveToNext();
					}			
					// Always close the cursor		
					cursor.close();
				}
			}				
		});

		ListView lv = (ListView) findViewById(R.id.listView1);

		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] {TrendingSitesTable.COLUMN_SITE_ID};
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.row_title_test };

		getSupportLoaderManager().initLoader(0, null, this);

		adapter = new SimpleCursorAdapter(this, R.layout.test_list_row, 
				null, from, to, 0);
		lv.setAdapter(adapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_test, menu);
		return true;
	}


	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = TrendingSitesTable.ALL_COLUMNS;
		CursorLoader cursorLoader = new CursorLoader(this,
				TrendingSitesContentProvider.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}


	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		// TODO Auto-generated method stub
		adapter.swapCursor(data);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		adapter.swapCursor(null);
	}
}
