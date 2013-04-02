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
package com.parworks.mars.view.search;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.mars.MarsMenuFragment;
import com.parworks.mars.R;
import com.parworks.mars.model.sync.SyncHandler;
import com.parworks.mars.utils.JsonMapper;
import com.parworks.mars.utils.User;

public class SearchFragment extends MarsMenuFragment {

	private static final String TAG = "SearchFragment";	
	
	private AutoCompleteTextView autoCompleteView;
	private Fragment popularSearchFragment;
	private Fragment searchResultFragment;

	private List<String> allTags;

	public SearchFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// retrieve the tags for search
		setRetainInstance(false);
		SharedPreferences myPrefs = this.getActivity().getSharedPreferences("MARSTAGS", 0);
		String allTags = myPrefs.getString("allTags", "[]");
		try {
			Log.d(TAG, "Loading all tags: " + allTags);
			this.allTags = JsonMapper.get().readValue(allTags, new TypeReference<List<String>>(){});
		} catch (IOException e) {
			Log.e(TAG, "Failed to parse the tags", e);
		}
	}
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		popularSearchFragment = new PopularSearchFragment(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ImageButton button = (ImageButton) 
				((SherlockFragmentActivity) this.getActivity())
						.getSupportActionBar().getCustomView().findViewById(R.id.rightBarButton);
		button.setBackgroundDrawable(null);
		
		// hide search result view
		if (searchResultFragment != null) {
			switchToSearchResult(true);
		} else {
			switchToSearchResult(false);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_search, null);	

		// config search text box
		autoCompleteView = (AutoCompleteTextView) v.findViewById(R.id.searchText);
		// config width/height
		@SuppressWarnings("deprecation")
		int w = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();		
		autoCompleteView.getLayoutParams().height = w * 92 / 640;
		// fill all the tags to assist input
		autoCompleteView.setAdapter(new ArrayAdapter<String>(
				this.getActivity(), R.layout.popular_searches_list_row, allTags));
		// handle click action
		autoCompleteView.setOnItemClickListener(new OnItemClickListener() {              
		    public void onItemClick (AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	triggerSearch(((TextView)selectedItemView).getText().toString());
		    }
		});
		
		// reset cursor whenever being clicked
		autoCompleteView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				autoCompleteView.setCursorVisible(true);
			}
		});		
		autoCompleteView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				autoCompleteView.setCursorVisible(true);
				return false;
			}
		});
		
		// handle key search action
		autoCompleteView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View arg0, int keyCode, KeyEvent event) {
            	if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
            		triggerSearch(autoCompleteView.getText().toString());
            		return true;
            	}
                return false;
            }
        });
		
		return v;
	}

	public void triggerSearchFromSuggestion(String searchWord) {
		// update text view
		autoCompleteView.setText(searchWord);
		autoCompleteView.setSelection(searchWord.length());		
		triggerSearch(searchWord);
	}
	
	private void triggerSearch(String searchWord) {
		Log.d(TAG, "Start searching: " + searchWord);
		// hide keyboard
		hideKeyBoard();
        
		// search 
		User.getARSites().searchSitesByTag(searchWord, new ARListener<List<String>>() {			
			@Override
			public void handleResponse(List<String> resp) {
				// start sync all site info at the background
				SyncHandler.syncListSiteInfo(resp);
				// show result 
				displaySearchResult(resp);
			}
		}, new ARErrorListener() {			
			@Override
			public void handleError(Exception e) {
				Log.e(TAG, "Failed to search the result", e);
			}
		});
	}
	
	private void displaySearchResult(List<String> siteIds) {
		Log.d(TAG, "Search found the following sites: " + siteIds);
		if (siteIds != null && siteIds.size() > 0) {
			autoCompleteView.setCursorVisible(false);
			searchResultFragment = new SearchResultFragment(siteIds);				
			// show the result view fragment
			switchToSearchResult(true);
		} else {
			Toast.makeText(this.getActivity(), "No results found.", Toast.LENGTH_LONG).show();
		}
	}
	
	private void hideKeyBoard() {
		// close key board
		InputMethodManager in = (InputMethodManager) this.getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(autoCompleteView.getApplicationWindowToken(),
        InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		hideKeyBoard();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();	
		FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();
		if (popularSearchFragment != null) {
			ft.remove(popularSearchFragment);
		}
		if (searchResultFragment != null) {
			ft.remove(searchResultFragment);
		}
		ft.commitAllowingStateLoss();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	/**
	 * Change the Fragment component in the main activity 
	 */
	public void switchToSearchResult(boolean showResult) {		
		if (showResult) {
			this.getActivity().getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.search_content_frame, searchResultFragment)
					.commit();
		} else {
			this.getActivity().getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.search_content_frame, popularSearchFragment)
					.commit();
		}
	}
	
	public Fragment getPopularSearchFragment() {
		return popularSearchFragment;
	}
	
	public Fragment getSearchResulFragment() {
		return searchResultFragment;
	}
}
