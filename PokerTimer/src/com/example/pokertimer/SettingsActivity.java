package com.example.pokertimer;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
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
    public static final String KEY_USE_LOCKSCREEN = "use_lockscreen";
    
    MyPreferenceFragment preferenceFragment;
    
    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        preferenceFragment = new MyPreferenceFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, preferenceFragment ).commit();
        
        //Action bar customize
        ActionBar ab = getActionBar();
        setTitle(getString(R.string.settings));
    	ab.setDisplayHomeAsUpEnabled(true);
    }
	
    //When is back button in actionbar pressed
    public boolean onOptionsItemSelected(MenuItem menuItem){       
        super.onBackPressed();
        return true;
    }
	 
	@Override
	protected void onResume() {
	        super.onResume();
	        // Setup the initial values
	        // Set up a listener whenever a key changes
	        PreferenceManager.getDefaultSharedPreferences(this)
	                .registerOnSharedPreferenceChangeListener(this);
	        preferenceFragment.showSummaries();
	    }

	    @Override
	    protected void onPause() {
	        super.onPause();
	        // Unregister the listener whenever a key changes
	        PreferenceManager.getDefaultSharedPreferences(this)
	                .unregisterOnSharedPreferenceChangeListener(this);
	    }

	public SharedPreferences getPref(){
	    return PreferenceManager.getDefaultSharedPreferences(this);
	}
	    
    public static class MyPreferenceFragment extends PreferenceFragment{
        RingtonePreference warningSound;
        
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            warningSound = (RingtonePreference)findPreference(KEY_WARNING_SOUND);
            
            showSummaries();
            
            
        }
        
        private void showSummaries(){
            if(warningSound != null){
                String strRingtonePreference = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(KEY_WARNING_SOUND, "default");
                Uri ringtoneUri = Uri.parse(strRingtonePreference);
                Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), ringtoneUri);
               
                warningSound.setSummary(ringtone.getTitle(getActivity()));
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        preferenceFragment.showSummaries();
    }
}
