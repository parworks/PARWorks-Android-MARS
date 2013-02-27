package com.parworks.mars.view.nearby;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.FrameLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.R;
import com.parworks.mars.view.nearby.GetLocation.GetLocationListener;
import com.parworks.mars.view.nearby.NearbySitesInfoFinder.NearbySitesInfoFinderListener;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

@SuppressLint("ValidFragment")
public class NearbyFragment extends SherlockFragment {
	
	public static final String TAG = NearbyFragment.class.getName();
	private SlidingFragmentActivity mSlidingFragmentActivity;
	
	private NearbySitesListFragment mNearbySitesListFragment;
	
	private GoogleMap mMap;
	private NearbySitesInfoFinder mInfoFinder;
	private Location mCurrentLocation;
	
	private static final int DEFAULT_MAX_SITES = 10;
	private static final double DEFAULT_RADIUS_IN_METERS = 1609; //about a mile;
	private static final float DEFAULT_ZOOM_LEVEL = 14.0f;
	
	private SupportMapFragment mMapFragment;
	
	private View mNearbyView;
	
	private Menu mMenu;
	
	
	public NearbyFragment() {
		super();
	}

	public NearbyFragment(SlidingFragmentActivity slidingFragmentActivity) {
		super();
		mSlidingFragmentActivity = slidingFragmentActivity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,"onCreate");
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG,"onCreateOptionsMenu");
		inflater.inflate(R.menu.fragment_nearby_sites, menu);
		setMenuVisibility(true);
		mMenu = menu;
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.nearby_sites_up_navigation:
			showNearbyList();
			return true;		
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setMenuVisibility(true);
		View v = inflater.inflate(R.layout.fragment_nearby, null);
		mMapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.fragmentNearbySitesMap);
		mMap = mMapFragment.getMap();
		mNearbySitesListFragment = new NearbySitesListFragment();
		mSlidingFragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.nearby_list_content_frame, mNearbySitesListFragment).commit();
		mMap.setMyLocationEnabled(true);
		mInfoFinder = new NearbySitesInfoFinder(new NearbySitesInfoFinderListener() {
			
			@Override
			public void gotSite(SiteInfo site) {
				gotNewSiteInfo(site);
				mNearbySitesListFragment.gotNewSiteInfo(site);
			}
		});
		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			
			@Override
			public void onCameraChange(CameraPosition position) {
				onCameraPositionChanged(position);
				
			}
		});
		mMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng point) {
				hideNearbyList();
				
			}
		});
		mMapFragment.getView().setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				makeMapFullScreen();
				return false;
			}
		});
		searchForLocation();
		mNearbyView = v;
		FrameLayout frameLayout = (FrameLayout) mNearbyView.findViewById(R.id.nearby_list_content_frame);
		frameLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideNearbyList();
				
			}
		});
		return v;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		searchForLocation();
	}
	private void onCameraPositionChanged(CameraPosition position) {
		mInfoFinder.getNearbySiteInfo(position.target, DEFAULT_MAX_SITES, DEFAULT_RADIUS_IN_METERS);
	}
	private void makeMapFullScreen() {
		LayoutParams newParams = mMapFragment.getView().getLayoutParams();
		newParams.height = LayoutParams.MATCH_PARENT;
		mMapFragment.getView().setLayoutParams(newParams);
	}
	private void hideNearbyList() {
		FrameLayout frameLayout = (FrameLayout) mNearbyView.findViewById(R.id.nearby_list_content_frame);
		frameLayout.setVisibility(View.GONE);
		mMenu.findItem(R.id.nearby_sites_up_navigation).setVisible(true);
	}
	private void showNearbyList() {
		FrameLayout frameLayout = (FrameLayout) mNearbyView.findViewById(R.id.nearby_list_content_frame);
		frameLayout.setVisibility(View.VISIBLE);
		mMenu.findItem(R.id.nearby_sites_up_navigation).setVisible(false);
	}

	private void searchForLocation() {
		GetLocation getLocation = new GetLocation(getActivity(),new GetLocationListener() {

			

			@Override
			public void searchingForLocation() {
			//	Toast.makeText(mContext, "Searching for gps location!", Toast.LENGTH_LONG).show();

			}

			@Override
			public void gotLocation(Location location) {
				mCurrentLocation = location;
				gotUserLocation(location);
			}
		});
		Location location = getLocation.start();
		gotUserLocation(location);

	}
	private void gotUserLocation(Location location) {
		LatLng latLon = new LatLng(location.getLatitude(), location.getLongitude());
		mInfoFinder.getNearbySiteInfo(latLon, DEFAULT_MAX_SITES, DEFAULT_RADIUS_IN_METERS);
		moveCamera(location,DEFAULT_ZOOM_LEVEL);
	}
	private void gotNewSiteInfo(SiteInfo info) {
		createSiteMarker(info);
	}
	private void createSiteMarker(SiteInfo info) {
		MarkerOptions markerOptions = new MarkerOptions();
		String latString = info.getLat();
		String lngString = info.getLon();
		double latDouble = Double.parseDouble(latString);
		double lngDouble = Double.parseDouble(lngString);
		markerOptions.position(new LatLng(latDouble,lngDouble));
		markerOptions.title(info.getId());
		markerOptions.snippet(info.getDescription());
		mMap.addMarker(markerOptions);
	}
	private void moveCamera(Location location, float zoom) {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
	}
}
