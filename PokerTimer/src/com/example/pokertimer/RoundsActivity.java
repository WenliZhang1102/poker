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

public class RoundsActivity extends ListActivity implements AdapterView.OnItemClickListener {
	private DataSource datasource;

	private List<Round> rounds;
	private ArrayAdapter<Round> adapter;
	
	/**
	 * Set Activity content
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		this.datasource = datasource;
		
		createRoundsList(); 
	}

	/**
	 * Shows list of games
	 */
	private void createRoundsList(){
		/*rounds = datasource.getAllRounds();
		adapter = new ArrayAdapter<Round>(this, android.R.layout.simple_list_item_1, rounds);
		setListAdapter(adapter);*/
	}
	
	/**
	 * Create context menu for list of games
	 */
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.games_actions, menu);
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
} 