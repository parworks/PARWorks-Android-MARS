package com.parworks.mars.view.technology;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

import com.parworks.mars.R;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class TechnologyFragment extends Fragment {
	
	public TechnologyFragment(SlidingFragmentActivity context) {
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_technology, null);
		WebView webView = (WebView) v.findViewById(R.id.technologyWebView);
		webView.setWebViewClient(new TechnologyWebViewClient());
		// how to enable the webview plugin changed in API 8
		webView.getSettings().setPluginsEnabled(true); 
		webView.getSettings().setJavaScriptEnabled(true); 
		webView.getSettings().setAllowFileAccess(true);
		
		webView.getSettings().setPluginState(PluginState.ON);
		webView.loadUrl("file:///android_asset/technology.html");
		return v;
	}
}
