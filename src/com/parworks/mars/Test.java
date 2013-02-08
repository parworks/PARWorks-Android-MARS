package com.parworks.mars;

import android.content.Intent;
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

import com.parworks.mars.model.db.TrendingSitesTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.parworks.mars.model.sync.SyncHelper;
import com.parworks.mars.view.siteexplorer.ExploreActivity;

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
				SyncHelper.sync();
			}
		});

		Button b2 = (Button) findViewById(R.id.buttonPrint);
		b2.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				String[] projection = TrendingSitesTable.ALL_COLUMNS;
				Cursor cursor = getContentResolver().query(MarsContentProvider.CONTENT_URI_ALL_TRENDING_SITES, 
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
				
				Intent i = new Intent(Test.this, ExploreActivity.class);
				i.putExtra(ExploreActivity.SITE_ID_ARGUMENT_KEY, "FirstSite");
				startActivity(i);
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
				MarsContentProvider.CONTENT_URI_ALL_TRENDING_SITES, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		adapter.swapCursor(data);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}
}
