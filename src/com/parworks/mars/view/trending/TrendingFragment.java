package com.parworks.mars.view.trending;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parworks.mars.model.db.TrendingSitesTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * The Trending view is based on a ViewPager,
 * which contains a list of TrendingSiteFragment.
 * 
 * @author yusun
 */
public class TrendingFragment extends Fragment implements LoaderCallbacks<Cursor> {	
	
	private static final String TAG = "TrendingFragment";
	private static final int TRENDING_SITES_LOADER_ID = 5;

	private SlidingFragmentActivity mContext;
	private ViewPager vp;
	private TrendingPagerAdapter vpAdapter;
	
	public TrendingFragment(SlidingFragmentActivity context) {
		super();
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
System.out.println("OnCreateView");
		vp = new ViewPager(this.getActivity());
		vp.setId("VP".hashCode());		
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) { }

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }

			@Override
			public void onPageSelected(int position) {		
				switch (position) {
				case 0:
					mContext.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					mContext.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}
		});		
		
		// config SlidingMenu touch mode
		mContext.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		return vp;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// init adapter			
		vpAdapter = new TrendingPagerAdapter(this.getActivity().getSupportFragmentManager());
		// init Loader for TrendingSites
		this.getLoaderManager().initLoader(TRENDING_SITES_LOADER_ID, null, this);		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		String[] projection = TrendingSitesTable.ALL_COLUMNS;
		CursorLoader cursorLoader = new CursorLoader(this.getActivity(), 
				MarsContentProvider.CONTENT_URI_ALL_TRENDING_SITES, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data.getCount() == 0) {
			return;
		}
		
		Log.d(TAG, "onLoadFinished and update sites fragment");
		// init new data content for the adapter
		ArrayList<Fragment> sitesFragments = new ArrayList<Fragment>(data.getCount());
		while(!data.isAfterLast()) {
			String siteId = data.getString(data.getColumnIndex(TrendingSitesTable.COLUMN_SITE_ID));
			int siteNum = data.getInt(data.getColumnIndex(TrendingSitesTable.COLUMN_NUM_AUGMENTED_IMAGES));
			sitesFragments.add(new TrendingSiteFragment(siteId, siteNum, 0));
			data.moveToNext();
		}	
		
		// update vp adapter
		vpAdapter.updateFragments(sitesFragments);	
		vp.setAdapter(vpAdapter);
		vp.setCurrentItem(0);
		vpAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		
	}
	
	
	/**
	 * Adapter for trending sites
	 */
	public class TrendingPagerAdapter extends FragmentPagerAdapter {
		
		private ArrayList<Fragment> mFragments;
		
		public TrendingPagerAdapter(FragmentManager fm) {
			super(fm);
			mFragments = new ArrayList<Fragment>();
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}
		
		@Override
		public int getItemPosition(Object item) {
			int position = mFragments.indexOf(item);
			if (position < 0) {
				position = POSITION_NONE;
			}
			return position;
		}

		public void updateFragments(ArrayList<Fragment> fragments) {
			mFragments.clear();
			mFragments = fragments;				
		}		
	}
}
