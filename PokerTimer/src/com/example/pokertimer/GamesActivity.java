package com.example.pokertimer;
import java.lang.reflect.Field;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GamesActivity extends ListActivity implements AdapterView.OnItemClickListener {
	private static final int ACTIVITY_ADD_GAME = 0;

	private DataSource datasource;

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
		getOverflowMenu();
	     
	}
	
	/**
	 * We will get overflow dots even if we have HW menu button
	 */
	private void getOverflowMenu() {

	     try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Shows list of games
	 */
	private void createGamesList(){
		datasource = new DataSource(this);
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
        //getMenuInflater().inflate(R.menu.games_actions, menu);
        
        /*AdapterView.AdapterContextMenuInfo info;
        try {
            // Casts the incoming data object into the type for AdapterView objects.
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            // If the menu object can't be cast, logs an error.
            Log.e("bad menuInfo", "bad menuInfo", e);
            return;
        }
        ListAdapter listAdapter =  getListAdapter();
        Cursor cursor = (Cursor) listAdapter.getItem(info.position);
        if (cursor == null) {
            // For some reason the requested item isn't available, do nothing
            return;
        }

        menu.setHeaderTitle(getString(cursor.getColumnIndex("name")));*/
        //TODO: problem je v blbem adapteru, tery je nastaveny pomoci setListAdapter - vraci game a ten nejde prejebnout na cursor
        //java.lang.ClassCastException: com.example.pokertimer.Game cannot be cast to android.database.Cursor
        //http://stackoverflow.com/questions/9447565/set-title-of-context-menu-from-the-selected-listview-item
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
		startActivityForResult(intent, ACTIVITY_ADD_GAME);
	}
	
	/**
	 * Switch to AboutActivity
	 */
	
	protected void startAboutActivity(){
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Switch to CountDownActivity
	 */
	protected void startCountdownActivity(Game game){
		Intent intent = new Intent(this, GameCountdownActivity.class);
		game.setRounds(datasource.getAllRounds(game));
		intent.putExtra("Game", game);
		startActivity(intent);
	} 
	
	public void onItemClick(AdapterView<?> parent, View v, int position, long id){
		startCountdownActivity(games.get(position));
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
			case R.id.menu_about:
				startAboutActivity();
			break;
			
			default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case (ACTIVITY_ADD_GAME) : {
				if (resultCode == Activity.RESULT_OK) {
					Game newGame = (Game) data.getSerializableExtra("Game");
					Game g = datasource.createGame(newGame);
				    adapter.add(g);
				}
				break;
			}
		}
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