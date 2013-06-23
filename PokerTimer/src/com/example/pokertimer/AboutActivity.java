package com.example.pokertimer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class AboutActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		
		
		ActionBar ab = getActionBar();
        setTitle(getString(R.string.about));
		ab.setDisplayHomeAsUpEnabled(true);

	     
	}
	
	//When is back button in actionbar pressed
	 public boolean onOptionsItemSelected(MenuItem menuItem)
	    {       
	        startActivity(new Intent(AboutActivity.this,GamesActivity.class)); 
	        return true;
	    }

}
