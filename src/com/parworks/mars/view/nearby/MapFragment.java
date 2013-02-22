package com.parworks.mars.view.nearby;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.R;
import com.parworks.mars.view.nearby.GetLocation.GetLocationListener;
import com.parworks.mars.view.nearby.NearbySitesInfoFinder.NearbySitesInfoFinderListener;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {
	public static final String TAG = MapFragment.class.getName();
	private GoogleMap mMap;
	private NearbySitesListFragment mNearbySitesListFragment;
	private NearbySitesInfoFinder mInfoFinder;
	
	private Location mCurrentLocation;
	private static final int DEFAULT_MAX_SITES = 10;
	private static final double DEFAULT_RADIUS_IN_METERS = 1609; //about a mile;
	
	public MapFragment(NearbySitesListFragment nearbySitesListFragment) {
		super();
		mNearbySitesListFragment = nearbySitesListFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_nearby_map, null);
		mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.fragmentNearbySitesMap)).getMap();
		mMap.setMyLocationEnabled(true);
		mInfoFinder = new NearbySitesInfoFinder(new NearbySitesInfoFinderListener() {
			
			@Override
			public void gotSite(SiteInfo site) {
				gotNewSiteInfo(site);
				
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
		mInfoFinder.getNearbySiteInfo(location, DEFAULT_MAX_SITES, DEFAULT_RADIUS_IN_METERS);
	}
	private void gotNewSiteInfo(SiteInfo info) {
		
	}
	private void displaySite(SiteInfo info) {
		
	}

}
