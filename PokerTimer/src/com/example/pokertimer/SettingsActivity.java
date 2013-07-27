package com.example.pokertimer;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	public static final String KEY_NEVER_OFF_SCREEN = "never_off_the_screen";
	public static final String KEY_WARNING_SOUND = "warning_sound";
    public static final String KEY_VIBRATIONS = "vibrations";
    public static final String KEY_NOTIFICATIONS = "notifications";
    
    RingtonePreference warningSound;
    
	@Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        
        //Action bar customize
        ActionBar ab = getActionBar();
        setTitle(getString(R.string.settings));
		ab.setDisplayHomeAsUpEnabled(true);
		
		//warningSound = (RingtonePreference) findPreference(KEY_WARNING_SOUND);
		showSummaries();
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_WARNING_SOUND)) {
            showSummaries();
        }
    }
    
    private void showSummaries(){
        /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        warningSound.setSummary(sharedPreferences.getString(KEY_WARNING_SOUND, ""));
        */
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Setup the initial values
        // Set up a listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
