package com.example.pokertimer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class AboutActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		
		
		ActionBar ab = getActionBar();
        setTitle(getString(R.string.about));
		ab.setDisplayHomeAsUpEnabled(true);

	     
	}
	
	 public void onClick(View v){
		 if(v == findViewById(R.id.donate)){
			 openDonateURL();
		 }
	 }
	
	public void openDonateURL() {
	    Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=7LNF5KQAFKG3C&lc=CZ&item_name=Poker%20countdown%20timer%20app&currency_code=CZK&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted" ) );

	    startActivity( browse );
	}
	
	//When is back button in actionbar pressed
	 public boolean onOptionsItemSelected(MenuItem menuItem)
	    {       
			 super.onBackPressed();
	 		return true;
	    }

}
