package com.example.pokertimer;

import java.util.Arrays;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AddGameActivity extends Activity {
	
	private static final int ACTIVITY_CHANGE_BLINDS = 10;

	private Game game;
	
	EditText textGameName; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_game);
        
        
        ActionBar ab = getActionBar();
        //ab.setDisplayShowHomeEnabled(false);
        setTitle(getString(R.string.add_game_title));
		ab.setDisplayHomeAsUpEnabled(true);
        //ab.setDisplayShowTitleEnabled(false);
        
        textGameName = (EditText) findViewById(R.id.game_name_edit); 
        
        game = new Game();
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
	    //When is back button in actionbar pressed
	      	  case android.R.id.home:
	      		  	startActivity(new Intent(AddGameActivity.this,GamesActivity.class));
		            return true;
		      case R.id.menu_ok:
		    	  saveNewGame();
		          break;
		      default:
		    	  cancel();
		          return super.onOptionsItemSelected(item);
	      }
	      return true;
	  }
	 
	 public void onClick(View v){
		 if(v == findViewById(R.id.modify_rounds)){
			 startRoundsActivity();
		 }
	 }
	 
	 protected void startRoundsActivity(){
			Intent intent = new Intent(this, RoundsActivity.class);
			startActivityForResult(intent, ACTIVITY_CHANGE_BLINDS);
	 }
	 
	 private void saveNewGame(){
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_OK, resultIntent);
		
		game.setName(textGameName.getText().toString());
		
		resultIntent.putExtra("Game", game);
		finish();
	 }
	 
	private void cancel(){
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case (ACTIVITY_CHANGE_BLINDS) : {
				if (resultCode == Activity.RESULT_OK) {
					Round[] rounds = (Round[]) data.getSerializableExtra("Rounds");
					game.setRounds(Arrays.asList(rounds));
				}
				break;
			}
		}
	}

}
