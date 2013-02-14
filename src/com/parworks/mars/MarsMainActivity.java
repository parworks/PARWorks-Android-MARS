package com.parworks.mars;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.parworks.mars.view.nearby.NearbyFragment;
import com.parworks.mars.view.search.SearchFragment;
import com.parworks.mars.view.trending.TrendingFragment;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * The main MARS activity that contains the sliding
 * menu, and all the fragment views for the main functions.
 * 
 * @author yusun
 */
public class MarsMainActivity extends SlidingFragmentActivity {

	private int mTitleRes;
	protected Fragment mFrag;
	private Fragment currentFragment;
	
	/**
	 *  The fragments controlled by the sliding menu,
	 *  including: 
	 *    1) Trending
	 *    2) Nearby
	 *    3) Search
	 *    4) Technology
	 */
	private Map<String, Fragment> menuControlledFragments;
	/** The framgent name keys */
	public static final String FRAGMENT_TRENDING = "Trending";
	public static final String FRAGMENT_NEARBY = "Nearby";
	public static final String FRAGMENT_SEARCH = "Search";
	public static final String FRAGMENT_TECHNOLOGY = "Technology";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set main activity view
		setContentView(R.layout.activity_base);		

		// set the Behind View (Sliding Menu)
		setBehindContentView(R.layout.menu_frame);
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		// init MenuFragment
		mFrag = new MainMenuListFragment();
		// set MenuFragment
		t.replace(R.id.menu_frame, mFrag);
		t.commit();

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		// configure ActionBar
		// enable the back arrow
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// setup the icon used for the action bar
		// getSupportActionBar().setIcon(R.drawable.ic_bar_item_sidebar);
		
		// init fragment components
		initMenuControlledFragments();
	}
	
	private void initMenuControlledFragments() {
		menuControlledFragments = new HashMap<String, Fragment>();
		
		// 1. Add Trending Fragment
		menuControlledFragments.put(FRAGMENT_TRENDING, new TrendingFragment(this));
		
		// 2. Add Nearby Fragment
		menuControlledFragments.put(FRAGMENT_NEARBY, new NearbyFragment(this));
		
		// 3. Add Search Fragment
		menuControlledFragments.put(FRAGMENT_SEARCH, new SearchFragment(this));
		// 4. Add Technology Fragment
		
		// Use Trending as the default view
		switchContent(FRAGMENT_TRENDING);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;		
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Change the Fragment component in the main activity 
	 */
	public void switchContent(final String fragmentName) {
		Fragment newFragment = menuControlledFragments.get(fragmentName);
		if (newFragment != null) {
			if (newFragment != currentFragment) {
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.content_frame, newFragment)
						.commit();
				currentFragment = newFragment;
			}
			// close menu and show the content fragment
			Handler h = new Handler();
			h.postDelayed(new Runnable() {
				public void run() {
					getSlidingMenu().showContent();
				}
			}, 50);
			// update ActionBar title
			this.getSupportActionBar().setTitle(fragmentName);
		}
	}
}
