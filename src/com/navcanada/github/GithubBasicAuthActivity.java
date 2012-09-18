package com.navcanada.github;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GithubBasicAuthActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		
    	EditText usernameEntry = (EditText)findViewById(R.id.usernameEntry);
    	EditText passwordEntry = (EditText)findViewById(R.id.passwordEntry);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        usernameEntry.setText(settings.getString("username", ""));
        passwordEntry.setText(settings.getString("password", ""));
		
		final Button okButton = (Button)findViewById(R.id.okButton);
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				GithubBasicAuthActivity.this.savePrefs();
				Intent intent = new Intent(GithubBasicAuthActivity.this, GithubTagsActivity.class);
	    		startActivity(intent);
			}
		});
	}
	
	public void savePrefs() {
    	EditText usernameEntry = (EditText)findViewById(R.id.usernameEntry);
    	EditText passwordEntry = (EditText)findViewById(R.id.passwordEntry);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", usernameEntry.getText().toString());
		editor.putString("password", passwordEntry.getText().toString());
		editor.commit();
	}
}