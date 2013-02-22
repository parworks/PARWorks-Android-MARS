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
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class NearbyFragment extends Fragment {
	
	public static final String TAG = NearbyFragment.class.getName();
	private SlidingFragmentActivity mSlidingFragmentActivity;
	
	private MapFragment mMapFragment;
	private NearbySitesListFragment mNearbySitesListFragment;
	
	
	public NearbyFragment() {
		super();
	}

	public NearbyFragment(SlidingFragmentActivity slidingFragmentActivity) {
		super();
		mSlidingFragmentActivity = slidingFragmentActivity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_nearby, null);
		mNearbySitesListFragment = new NearbySitesListFragment();
		mMapFragment = new MapFragment(mNearbySitesListFragment);
		mSlidingFragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.nearby_list_content_frame, mNearbySitesListFragment).commit();
		mSlidingFragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.nearby_map_content_frame, mMapFragment).commit();
		return v;
	}
}
