package com.parworks.mars;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainMenuListFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sliding_menu, null);
		
		// init the main menu options with an adapter
		ListView lv = (ListView) view.findViewById(R.id.menu_list);
		MainMenuListAdapter adapter = new MainMenuListAdapter(getActivity());
		adapter.add(new MainMenuItem(getString(R.string.menu_trending), 
				R.drawable.ic_menu_trending));
		adapter.add(new MainMenuItem(getString(R.string.menu_nearby), 
				R.drawable.ic_menu_nearby));
		adapter.add(new MainMenuItem(getString(R.string.menu_search), 
				R.drawable.ic_menu_search));
		adapter.add(new MainMenuItem(getString(R.string.menu_technology), 
				R.drawable.ic_menu_technology));		
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO
			}
		});

		return view;
	}

	private class MainMenuItem {
		public String tag;
		public int iconRes;
		public MainMenuItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	public class MainMenuListAdapter extends ArrayAdapter<MainMenuItem> {

		public MainMenuListAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.sliding_menu_list_row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}
	}
}
