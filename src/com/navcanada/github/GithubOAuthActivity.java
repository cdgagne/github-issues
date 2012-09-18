package com.navcanada.github;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/*public class GithubPrefsActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
	}
}*/

public class GithubOAuthActivity extends Activity {
	 
    public static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    public static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    
    public static String CLIENT_ID = "12144feb9c9e70f73274";
    public static String CLIENT_SECRET = "812cb650ef86d2247a356905d63c9e779106ea36";
    public static String CALLBACK_URL = "http://localhost";

    private static SharedPreferences mSettings = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        
        mSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	SharedPreferences.Editor editor = mSettings.edit();

		editor.putString("accessToken", "");
		editor.putString("accessCode", "");
		editor.commit();
    	
        String url = OAUTH_URL + "?client_id=" + CLIENT_ID;
        
        WebView webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String accessTokenFragment = "access_token=";
                String accessCodeFragment = "code=";

            	Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
                
            	// We hijack the GET request to extract the OAuth parameters

            	if (url.contains(accessTokenFragment)) {
            		// the GET request contains directly the token
            		String accessToken = url.substring(url.indexOf(accessTokenFragment) + accessTokenFragment.length());
            		GithubOAuthActivity.this.setAccessToken(accessToken);
            		
            		Toast.makeText(getApplicationContext(), "accessToken: " + accessToken, Toast.LENGTH_SHORT).show();
    				
            		//Intent intent = new Intent(GithubOAuthActivity.this, GithubTagsActivity.class);
    	    		//startActivity(intent);

            	} else if (url.contains(accessCodeFragment)) {
            		// the GET request contains an authorization code
            		String accessCode = url.substring(url.indexOf(accessCodeFragment) + accessCodeFragment.length());
            		GithubOAuthActivity.this.setAccessCode(accessCode);

            		Toast.makeText(getApplicationContext(), "accessCode:" + accessCode, Toast.LENGTH_SHORT).show();

            		String query = "client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=" + accessCode;
            		view.postUrl(OAUTH_ACCESS_TOKEN_URL, query.getBytes());
            	}
            }
        });
        webview.loadUrl(url);
    }
    
    public void setAccessToken(String accessToken) {
    	SharedPreferences.Editor editor = mSettings.edit();
    	editor.putString("accessToken", accessToken);
    	editor.commit();
    }
    
    public String getAccessToken() {
    	return mSettings.getString("accessToken", "");
    }
    
    public void setAccessCode(String accessCode) {
    	SharedPreferences.Editor editor = mSettings.edit();
    	editor.putString("accessCode", accessCode);
    	editor.commit();
    }
    
    public String getAccessCode() {
    	return mSettings.getString("accessCode", "");
    }
}