package com.example.pokertimer;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class AddGameActivity extends Activity {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_game);
        
        
        ActionBar ab = getActionBar();
        ab.setDisplayShowHomeEnabled(false);
        setTitle(getString(R.string.add_game_title));
        //ab.setDisplayShowTitleEnabled(false);
    }
	
	 @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.add_game, menu);
	      return true;
	  }
	 
	 @Override
	  public boolean onOptionsItemSelected(MenuItem item) {

	      switch (item.getItemId()) {
		      case R.id.menu_ok:    
		    	  //startAddGameActivity();
		          break;
		      case R.id.menu_cancel:
		    	  finish();
		    	  break;
		      default:
		          return super.onOptionsItemSelected(item);
	      }
	      return true;
	  }

}
