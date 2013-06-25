package com.example.pokertimer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class RoundsActivity extends ListActivity {
	
	private Round[] rounds;
	
	/**
	 * Set Activity content
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.blind_control);

        setDefaultRounds();
        
		ArrayAdapter<Object> adp = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, rounds);
        setListAdapter(adp);
        
        ActionBar ab = getActionBar();
        setTitle(getString(R.string.modify_rounds));
		ab.setDisplayHomeAsUpEnabled(true);
	}

	private void setDefaultRounds() {
		rounds = new Round[]{
				new Round(1, 25, 50, 0, 1200),
				new Round(2, 50, 100, 0, 1200),
				new Round(3, 100, 200, 0, 1200),
				new Round(4, 150, 300, 0, 1200),
				new Round(5, 200, 400, 0, 1200),
				new Round(6, 300, 600, 0, 1200),
				new Round(7, 400, 800, 0, 1200),
		};
	}
	
	private void saveModifiedBlinds(){
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);		
		resultIntent.putExtra("Rounds", rounds);
		finish();
	 }
} 