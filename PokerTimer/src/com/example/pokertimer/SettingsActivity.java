package com.example.pokertimer;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {
	
	 // @SuppressWarnings("deprecation")
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       addPreferencesFromResource(R.xml.preferences);
	        
	       
	        
	        ActionBar ab = getActionBar();
	        ab.setDisplayShowHomeEnabled(false);
	        ab.setDisplayShowTitleEnabled(false);
	    }

}
