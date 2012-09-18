package com.navcanada.github;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GithubIssuesActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	try {
    		Bundle b = getIntent().getExtras();
    		String tag = b.getString("tag");
    		
    		Toast.makeText(getApplicationContext(), tag, Toast.LENGTH_SHORT).show();
    		
    		setContentView(R.layout.issuelist);
    		ListView issueListView = (ListView)findViewById(R.id.issueList);
    		
    		List<Issue> issueList = getIssueList(tag);
    		
    		IssueAdapter arrayAdapter = new IssueAdapter(GithubIssuesActivity.this, R.layout.issuelistitem, issueList);
    		issueListView.setAdapter(arrayAdapter);
    		arrayAdapter.notifyDataSetChanged();
    		
			issueListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Integer issue = ((Issue)parent.getAdapter().getItem(position)).number;
					String url = "https://github.com/navcanada/cfps/issues/" + issue.toString();
					Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(GithubIssuesActivity.this, GithubWebviewActivity.class);
					Bundle b = new Bundle();
					b.putString("url", url);
					intent.putExtras(b);
					startActivity(intent);
				}
			});
    	} catch (Exception e) {
    		String errorMessage = e.getMessage();
    		Log.d("Error: ", errorMessage);
    		Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    	}
    }

    public List<Issue> getIssueList(String tag) {
    	List<Issue> issueList = null;
    	try {
    		String json = "Nothing";
    		BufferedReader in = null;
    		HttpClient client = new DefaultHttpClient();
    		HttpGet request = new HttpGet();
    	
    		URI uri = new URI("https://api.github.com/repos/navcanada/cfps/issues?state=closed&labels=" + tag);
    		
    		request.setURI(uri);
    		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    		String username = settings.getString("username", "");
    		String password = settings.getString("password", "");
    		String auth = username + ":" + password;
    		int flags = Base64.NO_WRAP | Base64.URL_SAFE;
    		request.addHeader("Authorization", "Basic " + Base64.encodeToString(auth.getBytes(), flags));
    		HttpResponse response = client.execute(request);
    		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    		StringBuffer sb = new StringBuffer("");
    		String line = "";
    		while ((line = in.readLine()) != null) {
    			sb.append(line);
    		}
    		json = sb.toString();

    		Type issueListType = new TypeToken<List<Issue>>(){}.getType(); 
    		Gson gson = new Gson();
    		issueList = gson.fromJson(json, issueListType);
    	} catch(Exception e) {
    		
    	}
		return issueList;
    }
}