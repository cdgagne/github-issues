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

public class GithubTagsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

    	if (!settings.contains("username")) {
    		Intent intent = new Intent(GithubTagsActivity.this, GithubBasicAuthActivity.class);
    		startActivity(intent);
    	}
/*    	if (!settings.contains("accessToken")) {
    		Intent intent = new Intent(GithubTagsActivity.this, GithubOAuthActivity.class);
    		startActivity(intent);
    	}*/
    	else {
    		try {
    			setContentView(R.layout.taglist);
    			ListView tagListView = (ListView)findViewById(R.id.tagList);
    			List<Tag> tagList = getTagList();
    			TagAdapter arrayAdapter = new TagAdapter(GithubTagsActivity.this, R.layout.taglistitem, tagList);
    			tagListView.setAdapter(arrayAdapter);
    			arrayAdapter.notifyDataSetChanged();

    			tagListView.setOnItemClickListener(new OnItemClickListener() {
    				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    					String tag = ((Tag)parent.getAdapter().getItem(position)).name;
    					Intent intent = new Intent(GithubTagsActivity.this, GithubIssuesActivity.class);
    					Bundle b = new Bundle();
    					b.putString("tag", tag);
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
    }
    
    public List<Tag> getTagList() {
    	List<Tag> tagList = null;
    	try {
    		String json = "Nothing";
    		BufferedReader in = null;
    		HttpClient client = new DefaultHttpClient();
    		HttpGet request = new HttpGet();
    	
    		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    		
    		URI uri = new URI("https://api.github.com/repos/navcanada/cfps/labels");
    		request.setURI(uri);

    		String username = settings.getString("username", "").toString();
    		String password = settings.getString("password", "").toString();
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

    		Type tagListType = new TypeToken<List<Tag>>(){}.getType(); 
    		Gson gson = new Gson();
    		tagList = gson.fromJson(json, tagListType);
    	} catch(Exception e) {
    		
    	}
		return tagList;
    }
}
