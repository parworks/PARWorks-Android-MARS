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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.parworks.mars.R;
import com.parworks.mars.utils.JsonMapper;

public class PopularSearchFragment extends Fragment {

	private static final String TAG = "PopularSearchFragment";
	
	private List<String> suggestedTags;
	private SearchFragment parentFragment;

	public PopularSearchFragment() {
		super();
	}
	
	public PopularSearchFragment(SearchFragment parentFragment) {
		super();
		this.parentFragment = parentFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// retrieve the tags for search
		SharedPreferences myPrefs = this.getActivity().getSharedPreferences("MARSTAGS", 0);
		String suggestedTags = myPrefs.getString("suggestedTags", "[]");
		try {
			Log.d(TAG, "Loading suggested tags: " + suggestedTags);
			this.suggestedTags = JsonMapper.get().readValue(suggestedTags, new TypeReference<List<String>>(){});
		} catch (IOException e) {
			Log.e(TAG, "Failed to parse the tags", e);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_popular_searches, null);
		// config popular searches lists
		final ListView popularSearchList = (ListView) v.findViewById(R.id.popularSearchesList);
		popularSearchList.setAdapter(new ArrayAdapter<String>(
				this.getActivity(), R.layout.popular_searches_list_row, suggestedTags));
		popularSearchList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				String searchWord = (String) popularSearchList.getItemAtPosition(position);
				parentFragment.triggerSearchFromSuggestion(searchWord);
			}
		});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();		
	}
}
