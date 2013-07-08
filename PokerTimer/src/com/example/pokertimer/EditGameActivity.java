package com.example.pokertimer;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.commonsware.cwac.tlv.TouchListView;

public class EditGameActivity extends ListActivity {
	
	private Game game;
	private ArrayList<Round> rounds = null;
	
	private IconicAdapter adapter=null;
	
	EditText textGameName;
	
	private ArrayList<String> array;
	
	/**
	 * Set Activity content
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.blind_control);

        processIntent();
        
        textGameName = (EditText) findViewById(R.id.game_name_edit);
        
        TouchListView tlv=(TouchListView)getListView();
		adapter=new IconicAdapter();
		setListAdapter(adapter);
		
		tlv.setDropListener(onDrop);
		tlv.setRemoveListener(onRemove);
	}
	
	private TouchListView.DropListener onDrop=new TouchListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			Round item = adapter.getItem(from);
			
			adapter.remove(item);
			adapter.insert(item, to);
			
			int start, end;
			
			if(from < to){
				start = from;
				end = to;
			}else{
				start = to;
				end = from;
			}
			
			for(int i = start; i <= end; i++){
				adapter.getItem(i).setNumberOfRound(i + 1); // (we index from 1; 1st round, 2nd round...)
			}
		}
	};
	
	private TouchListView.RemoveListener onRemove=new TouchListView.RemoveListener() {
		@Override
		public void remove(int which) {
			adapter.remove(adapter.getItem(which));
			for(int i = which; i < adapter.getCount(); i++){
				adapter.getItem(i).setNumberOfRound(i + 1);
			}
		}
	};
	
	class IconicAdapter extends ArrayAdapter<Round> {
		IconicAdapter() {
			super(EditGameActivity.this, R.layout.row2, rounds);
		}
		
		public View getView(int position, View convertView,
												ViewGroup parent) {
			View row=convertView;
			
			if (row==null) {													
				LayoutInflater inflater=getLayoutInflater();
				
				row=inflater.inflate(R.layout.row2, parent, false);
			}
			
			TextView label=(TextView)row.findViewById(R.id.label);
			
			label.setText(rounds.get(position).toString());
			
			return(row);
		}
	}
	
	/**
	 * Processes intent
	 */
	private void processIntent(){
		Intent intent = getIntent();
		this.game = (Game) intent.getSerializableExtra("Game");
		this.rounds = this.game.getRounds();
		
		array = new ArrayList<String>();
		for(Round r : this.rounds){
			array.add(r.toString());
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.blind_control, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		
		if(itemId == android.R.id.home){
	    		return true;
		}else if(itemId == R.id.menu_ok){
		    	this.saveModifiedBlinds();
		}else{
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
		game.setName(textGameName.getText().toString());
		resultIntent.putExtra("Game", game);
		finish();
	}
}