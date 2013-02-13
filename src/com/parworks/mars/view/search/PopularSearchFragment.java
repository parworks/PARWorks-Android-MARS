package com.parworks.mars.view.search;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.parworks.mars.R;
import com.parworks.mars.utils.JsonMapper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PopularSearchFragment extends Fragment {

	private static final String TAG = "PopularSearchFragment";
	
	private List<String> suggestedTags;

	public PopularSearchFragment() {
		super();
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
		ListView popularSearchList = (ListView) v.findViewById(R.id.popularSearchesList);
		popularSearchList.setAdapter(new ArrayAdapter<String>(
				this.getActivity(), R.layout.popular_searches_list_row, suggestedTags));
		return v;
	}
}
