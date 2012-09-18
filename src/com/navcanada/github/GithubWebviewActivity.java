package com.navcanada.github;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.webkit.WebView;

public class GithubWebviewActivity extends Activity {
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        
		Bundle b = getIntent().getExtras();
		String url = b.getString("url");
       
        WebView webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String username = settings.getString("username", "").toString();
		String password = settings.getString("password", "").toString();

		Map<String, String> headers = new HashMap<String, String>();
		String auth = username + ":" + password;
		int flags = Base64.NO_WRAP | Base64.URL_SAFE;
		headers.put("Authorization", "Basic " + Base64.encodeToString(auth.getBytes(), flags));
		webview.loadUrl(url, headers);
    }
}