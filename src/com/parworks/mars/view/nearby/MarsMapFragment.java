package com.parworks.mars.view.nearby;

import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;

public class MarsMapFragment extends SupportMapFragment {

	public interface MarsMapCreatedListener {
		public void onInitialized();
	}
	
	private MarsMapCreatedListener initListener;
	
	public MarsMapFragment() {
		super();
	}
	
	public MarsMapFragment(MarsMapCreatedListener listener) {
		super();
		this.initListener = listener;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (initListener != null) {
			initListener.onInitialized();
		}
	}
}
