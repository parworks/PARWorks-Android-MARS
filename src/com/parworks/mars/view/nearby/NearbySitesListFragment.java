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
//				Intent i = new Intent(NearbySitesListFragment.this.getActivity(), ExploreActivity.class);
//				i.putExtra(ExploreActivity.SITE_ID_ARGUMENT_KEY, (String)view.getTag());
//				startActivity(i);				
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
