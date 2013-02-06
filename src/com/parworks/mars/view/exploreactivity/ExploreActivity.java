package com.parworks.mars.view.exploreactivity;

import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.ar.ARSite;
import com.parworks.androidlibrary.ar.ARSites;
import com.parworks.mars.R;
import com.parworks.mars.utils.User;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class ExploreActivity extends Activity {
	
	ImageView mSiteImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explore);
		
		mSiteImageView = (ImageView) findViewById(R.id.imageViewSiteImage);
	}
	
	public void loadSiteImage() {
		ARSites sites = new ARSites(User.getApiKey(),User.getSecretKey());
		sites.getExisting("FistSite",new ARListener<ARSite>() {

			@Override
			public void handleResponse(ARSite resp) {
				// TODO Auto-generated method stub
				
			}
			
		}, new ARErrorListener() {

			@Override
			public void handleError(Exception error) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

}
