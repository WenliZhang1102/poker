package com.example.pokertimer;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

public class RoundsActivity extends ListActivity {
	
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
		Game game = (Game) intent.getSerializableExtra("Game");
		this.rounds = game.getRounds();
	}
	
	private void saveModifiedBlinds(){
		/*Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
		resultIntent.putExtra("Rounds", rounds);
		finish();*/
	 }
} 