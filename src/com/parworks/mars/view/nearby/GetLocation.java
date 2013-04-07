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
package com.parworks.mars.view.nearby;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

public class GetLocation {
	private final Context mContext;
	private GetLocationListener mListener;
	
	public interface GetLocationListener {
		public void gotLocation(Location location);
		public void searchingForLocation();
	}
	
	public GetLocation(Context context, GetLocationListener listener) {
		mContext = context;
		mListener = listener;
	}
	public Location start() {
		findLocation();
		return getLastKnownLocation();
	}
	
	private void showEnableGpsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Enable GPS?");
		TextView message = new TextView(mContext);
		message.setText("Your GPS is disabled. Would you like to be taken to the settings menu to enable it or use your device's last known location?");
		builder.setView(message);
		builder.setNeutralButton("Use my last known location.", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				useLastKnownLocation();
				
			}
			
		});
		builder.setPositiveButton("Take me to location settings!", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
			
		});
		AlertDialog confirmDialog = builder.create();
		confirmDialog.show();
	}
	
	private void showGpsAlreadyEnabledDialog() {
		searchForLocation();
//		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//		builder.setTitle("Begin search?");
//		TextView message = new TextView(mContext);
//		message.setText("Your GPS is enabled! Should we begin searching for your location, or use your last known location?");
//		builder.setView(message);
//		builder.setNeutralButton("Use my last known location.", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				useLastKnownLocation();
//				
//			}
//			
//		});
//		builder.setPositiveButton("Begin the search!", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				searchForLocation();
//			}
//			
//		});
//		AlertDialog confirmDialog = builder.create();
//		confirmDialog.show();
	}
	
	private void useLastKnownLocation() {
		Location location = getLastKnownLocation();
		if(location == null ) {
			//Toast.makeText(mContext, "Sorry, you don't seem to have a last known location :( Choose the search option instead!",Toast.LENGTH_LONG).show();
			return;
		}
		mListener.gotLocation(location);
	}
	private Location getLastKnownLocation() {
		LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
		if(location == null ) {
			//Toast.makeText(mContext, "Sorry, you don't seem to have a last known location :( Choose the search option instead!",Toast.LENGTH_LONG).show();
			return null;
		} else {
			return location;
		}
	}
	
	private void searchForLocation() {
		final LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		mListener.searchingForLocation();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				locationManager.removeUpdates(this);
				mListener.gotLocation(location);
			}
		});
		
	}
	private void findLocation() {
		final LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(gpsEnabled) {
			showGpsAlreadyEnabledDialog();
		} else {
			showEnableGpsDialog();
		}
	}

}
