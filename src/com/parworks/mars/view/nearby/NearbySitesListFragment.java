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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.parworks.androidlibrary.response.SiteInfo;
import com.parworks.mars.R;
import com.parworks.mars.view.search.SearchResultAdapter;
import com.parworks.mars.view.search.SearchResultAdapter.ViewHolder;
import com.parworks.mars.view.siteexplorer.ExploreActivity;

public class NearbySitesListFragment extends Fragment {
	
	public static final String TAG = NearbySitesListFragment.class.getName();
	private SearchResultAdapter newAdapter;
	
	public NearbySitesListFragment() {
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search_result, null);
		// initialize search result		
		final ListView lv = (ListView) v.findViewById(R.id.searchResultList);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				Intent i = new Intent(NearbySitesListFragment.this.getActivity(), ExploreActivity.class);
				i.putExtra(ExploreActivity.SITE_ID_ARGUMENT_KEY, ((ViewHolder) view.getTag()).textView.getText());
				startActivity(i);				
			}
		});				
		
		newAdapter = new SearchResultAdapter(this.getActivity());		
		lv.setAdapter(newAdapter);
		
		v.requestFocus();
		return v;
	}
	public void gotNewSiteInfo(SiteInfo site) {
		newAdapter.updateRecord(site.getId(), site.getPosterImageURL());
		newAdapter.notifyDataSetChanged();
		
	}

}
