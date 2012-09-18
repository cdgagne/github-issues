package com.navcanada.github;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IssueAdapter extends ArrayAdapter<Issue> {
	int resource;
	String response;
	Context context;
	
	public IssueAdapter(Context context, int resource, List<Issue> items) {
		super(context, resource, items);
		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout issueView;
		Issue issue = getItem(position);
		
		if (convertView == null) {
			issueView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater)getContext().getSystemService(inflater);
			vi.inflate(resource, issueView, true);
		} else {
			issueView = (LinearLayout) convertView;
		}
		
		TextView issueTitle = (TextView)issueView.findViewById(R.id.txtIssueTitle);
		issueTitle.setText(issue.title);
		
		return issueView;
	}
}
