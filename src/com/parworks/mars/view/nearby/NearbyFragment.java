package com.parworks.mars.view.nearby;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parworks.mars.R;
import com.parworks.mars.view.nearby.GetLocation.GetLocationListener;

public class NearbyFragment extends Fragment {
	
	public static final String TAG = NearbyFragment.class.getName();
	private Context mContext;
	private GoogleMap mMap;
	
	private final double DEFAULT_RADIUS_IN_METERS = 1609.34; //one mile
	private final int DEFAULT_MAX_SITES = 10;
	
	private Location mCurrentLocation;
	
	public NearbyFragment() {
		super();
	}

	public NearbyFragment(Context context) {
		super();
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_nearby, null);
		mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.fragmentNearbySitesMap)).getMap();
		mMap.setMyLocationEnabled(true);
		mCurrentLocation = mMap.getMyLocation();
		return v;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		searchForLocation();
	}
	
	private void searchForLocation() {
		GetLocation getLocation = new GetLocation(mContext,new GetLocationListener() {
			
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
		moveCamera(location);
		displayNearbySites(location, DEFAULT_MAX_SITES,DEFAULT_RADIUS_IN_METERS);
	}
	private void displayNearbySites(Location location, int maxSites, double radius) {
		NearbySitesDisplayManager nearbySiteManager = new NearbySitesDisplayManager(mMap);
		nearbySiteManager.displayNearbySites(location, maxSites, radius);
	}
	
	private void moveCamera(Location location) {
		LatLng currentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, mMap.getMaxZoomLevel()));
	}
}
