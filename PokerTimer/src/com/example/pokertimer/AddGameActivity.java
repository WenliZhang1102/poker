package com.example.pokertimer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddGameActivity extends Activity {
	
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
		      case R.id.menu_cancel:
		    	  cancel();
		    	  break;
		      default:
		          return super.onOptionsItemSelected(item);
	      }
	      return true;
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

}
