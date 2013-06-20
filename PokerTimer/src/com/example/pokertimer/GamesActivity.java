package com.example.pokertimer;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class GamesActivity extends ListActivity {
	private GamesDataSource datasource;

	/**
	 * Set Activity content
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		createGamesList();  
	}

	/**
	 * Shows list of games
	 */
	private void createGamesList(){
		datasource = new GamesDataSource(this);
		datasource.open();
		
		List<Game> values = datasource.getAllGames();
		ArrayAdapter<Game> adapter = new ArrayAdapter<Game>(this,
		android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}
	
	/**
	 * Shows top menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.games, menu);
		return true;
	}
  
	/**
	 * Switch to settings activity
	 */
	protected void startSettingsActivity(){
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
  
	/**
	 * Switch to AddGameActivity
	 */
	protected void startAddGameActivity(){
		Intent intent = new Intent(this, AddGameActivity.class);
		startActivity(intent);
	}
  
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_add:    
				startAddGameActivity();
			break;
			
			case R.id.menu_search:
				// TODO: menu_search
			break;
			
			case R.id.menu_settings:
				startSettingsActivity();
			break;
			
			default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
} 