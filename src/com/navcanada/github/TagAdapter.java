package com.navcanada.github;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TagAdapter extends ArrayAdapter<Tag> {
	int resource;
	String response;
	Context context;
	
	public TagAdapter(Context context, int resource, List<Tag> items) {
		super(context, resource, items);
		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout tagView;
		Tag tag = getItem(position);
		
		if (convertView == null) {
			tagView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater)getContext().getSystemService(inflater);
			vi.inflate(resource, tagView, true);
		} else {
			tagView = (LinearLayout) convertView;
		}
		
		TextView tagText = (TextView)tagView.findViewById(R.id.txtTagName);
		Button tagColor = (Button)tagView.findViewById(R.id.txtTagColor);
		tagText.setText(tag.name);
		tagColor.setBackgroundColor(Color.parseColor("#" + tag.color));

		return tagView;
	}
}
