package com.example.pokertimer;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {
	
	@Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        
        //Action bar customize
        ActionBar ab = getActionBar();
        setTitle(getString(R.string.settings));
		ab.setDisplayHomeAsUpEnabled(true);
    }
	
	//When is back button in actionbar pressed
	 public boolean onOptionsItemSelected(MenuItem menuItem)
	    {       
	        startActivity(new Intent(SettingsActivity.this,GamesActivity.class)); 
	        return true;
	    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
