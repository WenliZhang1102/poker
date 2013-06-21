package com.example.pokertimer;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class GamesActivity extends ListActivity implements AdapterView.OnItemClickListener {
	private GamesDataSource datasource;

	private List<Game> games;
	private ArrayAdapter<Game> adapter;
	
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
		
		games = datasource.getAllGames();
		adapter = new ArrayAdapter<Game>(this, android.R.layout.simple_list_item_1, games);
		setListAdapter(adapter);
		
		ListView list = getListView();
        list.setOnItemClickListener(this);
        
		registerForContextMenu(list);
	}
	
	/**
	 * Create context menu for list of games
	 */
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.games_actions, menu);
    }
	
	/**
	 * Create actions for context menu in list of games
	 */
	@Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
 
        switch(item.getItemId()){
            case R.id.menu_edit:
            break;
            case R.id.menu_delete:
                datasource.deleteGame(games.get(info.position));
                adapter.remove(games.get(info.position));
            break;
        }
        
        adapter.notifyDataSetChanged();
        return true;
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
	
	protected void startCountdownActivity(){
		Intent intent = new Intent(this, GameCountdownActivity.class);
		startActivity(intent);
	} 
	
	public void onItemClick(AdapterView<?> parent, View v, int position, long id){
		startCountdownActivity();
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