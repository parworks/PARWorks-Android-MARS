package com.parworks.mars.view.nearby;

import java.util.HashMap;
import java.util.Map;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.MarsMenuFragment;
import com.parworks.mars.R;
import com.parworks.mars.view.nearby.GetLocation.GetLocationListener;
import com.parworks.mars.view.nearby.MarsMapFragment.MarsMapCreatedListener;
import com.parworks.mars.view.nearby.NearbySitesInfoFinder.NearbySitesInfoFinderListener;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class NearbyFragment extends MarsMenuFragment implements MarsMapCreatedListener {
	
	public static final String TAG = NearbyFragment.class.getName();
	public static final String TAG_LOAD_MARKERS = "LOADING MARKERS TAG";
	
	private NearbySitesListFragment mNearbySitesListFragment;
	
	private GoogleMap mMap;
	private NearbySitesInfoFinder mInfoFinder;
	
	private static final int DEFAULT_MAX_SITES = 10;
	private static final float MIN_ZOOM_TO_SHOW_MARKERS = 0.0f;
	private static final double DEFAULT_RADIUS_IN_METERS = 10000 ;//10km
	private static final float DEFAULT_ZOOM_LEVEL = 14.0f;
	
	private MarsMapFragment mMapFragment;
	
	private boolean isMapInitialized = false;
	
	private View mNearbyView;	
	
	private Map<String,Marker> mAllMarkers = new HashMap<String,Marker>();
	
	private boolean mIsMapFullscreen;
	
	
	public NearbyFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,"onCreate");
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
//        SupportMapFragment fragment = (SupportMapFragment) (getFragmentManager().findFragmentById(R.id.fragmentNearbySitesMap));  
//        if(fragment == null) {
//        	return;
//        }
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//        ft.remove(mMapFragment);
//        ft.commitAllowingStateLoss();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_nearby_test, null); 
		mNearbySitesListFragment = new NearbySitesListFragment();
				
		mNearbyView = v;
		return v;
	}
	
	protected void initializeMap() {
		mMap = mMapFragment.getMap();
		isMapInitialized = true;
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
		
		FrameLayout frameLayout = (FrameLayout) mNearbyView.findViewById(R.id.nearby_list_content_frame);
		frameLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideNearbyList();				
			}
		});		
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("YUSUN ONRESUME MAP");
		searchForLocation();
		ImageButton button = (ImageButton) ((SherlockFragmentActivity) this.getActivity())
				.getSupportActionBar().getCustomView().findViewById(R.id.rightBarButton);
		button.setBackgroundResource(R.drawable.ic_bar_item_reload);
		
		this.getActivity().getSupportFragmentManager()
				.beginTransaction().replace(R.id.nearby_list_content_frame, mNearbySitesListFragment).commit();
				
		mMapFragment = new MarsMapFragment(this);
		this.getActivity().getSupportFragmentManager()
				.beginTransaction().replace(R.id.nearby_map_containerasdf, mMapFragment).commit();			
	}
	
	public void rightBarButtonClicked(View v) {
		super.rightBarButtonClicked(v);
		if(mIsMapFullscreen)
			showNearbyList();
		else
			searchForLocation();
	}
	
	private void onCameraPositionChanged(CameraPosition position) {
		Log.d(TAG_LOAD_MARKERS, "onCameraPositionChanged");
		mInfoFinder.getNearbySiteInfo(position.target, DEFAULT_MAX_SITES, DEFAULT_RADIUS_IN_METERS);
		Log.d(TAG,"onCameraPositionChanged: " + position.zoom);
		if(position.zoom < MIN_ZOOM_TO_SHOW_MARKERS ) {
			removeAllSiteMarkers();
		}
	}
	
	private void makeMapFullScreen() {		
		LayoutParams newParams = mMapFragment.getView().getLayoutParams();
		newParams.height = LayoutParams.MATCH_PARENT;
		mMapFragment.getView().setLayoutParams(newParams);
	}
	
	private void hideNearbyList() {
		mIsMapFullscreen = true;
		ImageButton button = (ImageButton) ((SherlockFragmentActivity) this.getActivity())
				.getSupportActionBar().getCustomView().findViewById(R.id.rightBarButton);
		button.setBackgroundResource(R.drawable.ic_bar_item_up);
		FrameLayout frameLayout = (FrameLayout) mNearbyView.findViewById(R.id.nearby_list_content_frame);
		frameLayout.setVisibility(View.GONE);		
		((SlidingFragmentActivity) this.getActivity())
				.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}
	
	private void showNearbyList() {
		mIsMapFullscreen = false;
		ImageButton button = (ImageButton) ((SherlockFragmentActivity) this.getActivity())
				.getSupportActionBar().getCustomView().findViewById(R.id.rightBarButton);
		button.setBackgroundResource(R.drawable.ic_bar_item_reload);
		FrameLayout frameLayout = (FrameLayout) mNearbyView.findViewById(R.id.nearby_list_content_frame);
		frameLayout.setVisibility(View.VISIBLE);		
		((SlidingFragmentActivity) this.getActivity())
				.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	private void searchForLocation() {
		if(!isMapInitialized) {
			return;
		}
		GetLocation getLocation = new GetLocation(getActivity(),new GetLocationListener() {
			@Override
			public void searchingForLocation() {
				//	Toast.makeText(mContext, "Searching for gps location!", Toast.LENGTH_LONG).show();
			}

			@Override
			public void gotLocation(Location location) {
				if(location == null) {
					return;
				}
				gotUserLocation(location);
			}
		});
		
		Location location = getLocation.start();
		if(location == null) {
			return;
		}
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
		if(mMap.getCameraPosition().zoom < MIN_ZOOM_TO_SHOW_MARKERS) {
			return;
		}
		MarkerOptions markerOptions = new MarkerOptions();
		String latString = info.getLat();
		String lngString = info.getLon();
		double latDouble = Double.parseDouble(latString);
		double lngDouble = Double.parseDouble(lngString);
		markerOptions.position(new LatLng(latDouble,lngDouble));
		markerOptions.title(info.getId());
		markerOptions.snippet(info.getDescription());
		Marker marker = mMap.addMarker(markerOptions);
		mAllMarkers.put(info.getId(), marker);
	}
	
	private void removeSiteMarker(String siteId) {
		Log.d(TAG,"Removing site marker: " + siteId);
		mAllMarkers.get(siteId).remove();
	}
	
	private void removeAllSiteMarkers() {
		Log.d(TAG,"Removing all site markers");
		for(String key : mAllMarkers.keySet()) {
			removeSiteMarker(key);
		}
		mAllMarkers.clear();
		mMap.clear();
	}
	
	private void moveCamera(Location location, float zoom) {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
	}

	@Override
	public void onInitialized() {
		initializeMap();
	}
}
