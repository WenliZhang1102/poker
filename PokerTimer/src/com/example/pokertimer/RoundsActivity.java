package com.example.pokertimer;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class RoundsActivity extends ListActivity {
	
	private Game game;
	private List<Round> rounds;
	
	/**
	 * Set Activity content
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.blind_control);

        processIntent();
        
		BlindsAdapter adp = new BlindsAdapter(this, rounds);
        setListAdapter(adp);
        
        ActionBar ab = getActionBar();
        setTitle(getString(R.string.modify_rounds));
		ab.setDisplayHomeAsUpEnabled(true);
	}
	
	/**
	 * Processes intent
	 */
	private void processIntent(){
		Intent intent = getIntent();
		this.game = (Game) intent.getSerializableExtra("Game");
		this.rounds = this.game.getRounds();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.blind_control, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
	    //When is back button in actionbar pressed
	    	case android.R.id.home:
	    		return true;
		    case R.id.menu_ok:
		    	this.saveModifiedBlinds();
		        break;
		    default:
		    	return super.onOptionsItemSelected(item);
	    }
		
		return true;
	}
	
	/**
	 * Send modified blinds back to the AddGameActivity
	 */
	private void saveModifiedBlinds(){
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
		resultIntent.putExtra("Game", game);
		finish();
	}
}