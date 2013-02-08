package com.parworks.mars.view.siteexplorer;

import java.util.List;

import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.ar.ARSite;
import com.parworks.androidlibrary.ar.ARSites;
import com.parworks.androidlibrary.response.BaseImageInfo;
import com.parworks.mars.utils.ImageLoader;
import com.parworks.mars.utils.ImageLoader.ImageLoaderListener;
import com.parworks.mars.utils.User;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

public class BaseImageRetreiver {
	private Context mContext;
	
	public BaseImageRetreiver(Context context) {
		mContext = context;
	}
	
	public void setImageViewToBaseImage(String siteId, final ImageView imageView, final Activity activity,ImageLoaderListener listener) {
		setImageViewToBaseImage(siteId,imageView,activity,null,listener);
	}
	public void setImageViewToBaseImage(String siteId, final ImageView imageView, final Activity activity, final Integer imageWidth,final ImageLoaderListener listener) {
		ARSites sites = new ARSites(User.getApiKey(),User.getSecretKey());
		sites.getExisting(siteId, new ARListener<ARSite>() {
			
			@Override
			public void handleResponse(ARSite resp) {
				resp.getBaseImages(new ARListener<List<BaseImageInfo>>() {
					
					@Override
					public void handleResponse(List<BaseImageInfo> resp) {
						if(resp.size() > 0 ) {
							BaseImageInfo firstBaseImage = resp.get(0);
							ImageLoader imageLoader = new ImageLoader(mContext);
							imageLoader.DisplayImage(firstBaseImage.getFullSize(), activity, imageView,imageWidth,listener);
						}
						
					}
				}, new ARErrorListener() {
					
					@Override
					public void handleError(Exception error) {
						// TODO Auto-generated method stub
						
					}
				});
				
			}
		}, new ARErrorListener() {
			
			@Override
			public void handleError(Exception error) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
