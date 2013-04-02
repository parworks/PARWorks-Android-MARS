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
package com.parworks.mars.view.technology;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.parworks.mars.MarsMenuFragment;
import com.parworks.mars.R;

public class TechnologyFragment extends MarsMenuFragment {
	
	public TechnologyFragment() {
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_technology, null);
		WebView webView = (WebView) v.findViewById(R.id.technologyWebView);
//		webView.setWebViewClient(new TechnologyWebViewClient());
		// how to enable the webview plugin changed in API 8
		webView.getSettings().setPluginsEnabled(true); 
		webView.getSettings().setJavaScriptEnabled(true); 
		webView.getSettings().setAllowFileAccess(true);
		
		webView.getSettings().setPluginState(PluginState.ON_DEMAND);
		webView.getSettings().setPluginsEnabled(true);
		webView.loadUrl("file:///android_asset/technology.html");
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ImageButton button = (ImageButton) ((SherlockFragmentActivity) this.getActivity())
				.getSupportActionBar().getCustomView().findViewById(R.id.rightBarButton);
		button.setBackgroundResource(R.drawable.ic_bar_item_intro);		
	}
	
	public void rightBarButtonClicked(View v) {
		super.rightBarButtonClicked(v);
		Toast.makeText(this.getActivity(), "Insert Intro", Toast.LENGTH_SHORT).show();
	}
}
