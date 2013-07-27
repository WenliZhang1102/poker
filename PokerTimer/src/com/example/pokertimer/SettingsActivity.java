package com.example.pokertimer;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {
	public static final String KEY_NEVER_OFF_SCREEN = "never_off_the_screen";
	public static final String KEY_WARNING_SOUND = "warning_sound";
    
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
			 super.onBackPressed();
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
