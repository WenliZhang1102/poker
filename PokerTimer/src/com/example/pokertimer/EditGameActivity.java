package com.example.pokertimer;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.commonsware.cwac.tlv.TouchListView;

public class EditGameActivity extends ListActivity implements AdapterView.OnItemClickListener {
	
	private static final int ACTIVITY_EDIT_ROUND = 0;
	private Game game;
	private ArrayList<Round> rounds = null;
	
	private IconicAdapter adapter=null;
	
	private Boolean delete_visibility = false;
	
	private int delete_count = 0;
	
	private OnClickListener removeError = new OnClickListener(){
		@Override
		public void onClick(View v) {
			textGameName.setError(null);
		}
	};

	EditText textGameName;
	
	Button buttonDeleteCheckbox;
	
	private ArrayList<String> array;
	
	private OnClickListener checkboxclick = new View.OnClickListener() {  
		public void onClick(View v) {  
			// set round for delete
			CheckBox checkbox = (CheckBox) v;
			Round round = (Round) checkbox.getTag();  
			round.setForDelete(checkbox.isChecked());
			
			// update menu and buttons
			if(checkbox.isChecked()){
				delete_count++;
			}else{
				delete_count--;
			}
			
			refreshDeleteButton();
			
			invalidateOptionsMenu();
		}
	};
	
	private void refreshDeleteButton(){
		buttonDeleteCheckbox.setText(getText(R.string.delete) + " (" + delete_count + ")");
		
		if(delete_count == 0){
			buttonDeleteCheckbox.setEnabled(false);
		}else{
			buttonDeleteCheckbox.setEnabled(true);
		}
	}
	
	/**
	 * Set Activity content
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.blind_control);

        textGameName = (EditText) findViewById(R.id.game_name_edit);
        
        buttonDeleteCheckbox = (Button) findViewById(R.id.button_delete_checkbox);
        
        processIntent();
        
        TouchListView tlv=(TouchListView)getListView();
		adapter=new IconicAdapter();
		setListAdapter(adapter);
		
		tlv.setDropListener(onDrop);
		tlv.setOnItemClickListener(this);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setTitle(getResources().getString(R.string.edit_game));
	}
	
	/**
	 * Listener for moving rounds
	 */
	private TouchListView.DropListener onDrop = new TouchListView.DropListener() {
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
	
	/**
	 * Shows rows in table
	 * @author Alesh
	 *
	 */
	class IconicAdapter extends ArrayAdapter<Round> {
		IconicAdapter() {
			super(EditGameActivity.this, R.layout.row2, rounds);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			
			if (row == null) {											
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.row2, parent, false);
			}
			
			Round round = rounds.get(position);
			
			// set round number
			TextView round_number = (TextView)row.findViewById(R.id.round_number);
			round_number.setText(round.getNumerOfRound() + ".");
			
			// set round info
			TextView label = (TextView)row.findViewById(R.id.label);
			label.setText(round.toString());
			
			// set round time
			TextView round_time = (TextView)row.findViewById(R.id.round_time);
			round_time.setText(round.getReadableTime());
			
			// set checkbox
			CheckBox checkbox = (CheckBox) row.findViewById(R.id.delete_checkbox);
			
			// image
			ImageView icon = (ImageView) row.findViewById(R.id.icon);
			
			if(delete_visibility){
				checkbox.setVisibility(View.VISIBLE);
				icon.setVisibility(View.GONE);
				checkbox.setChecked(round.isForDelete());
			}else{
				checkbox.setVisibility(View.GONE);
				icon.setVisibility(View.VISIBLE);
			}
			
			checkbox.setTag(round);
			checkbox.setOnClickListener(checkboxclick);
			
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
		
		textGameName.setText(this.game.getName()+"");
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	    
	    if(delete_visibility == true)
	    	scaleDownListView();
	    else
	    	enlargeListView();
	  }
	
	private void showDeleteButtons(){
		findViewById(R.id.button_layout).setVisibility(View.VISIBLE);
		buttonDeleteCheckbox.setVisibility(View.VISIBLE);
		findViewById(R.id.button_cancel_checkbox).setVisibility(View.VISIBLE);
		
		refreshDeleteButton();
	}
	
	private void hideDeleteButtons(){
		// hide buttons
		findViewById(R.id.button_layout).setVisibility(View.INVISIBLE);
		buttonDeleteCheckbox.setVisibility(View.INVISIBLE);
		findViewById(R.id.button_cancel_checkbox).setVisibility(View.INVISIBLE);
	}
	
	private void enlargeListView(){
		View view = findViewById(android.R.id.list);
		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height = LayoutParams.MATCH_PARENT;
		view.setLayoutParams(params);
	}
	
	private void scaleDownListView(){
		View view = findViewById(android.R.id.list);
		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int display_height = size.y;
		
		int height;
		int [] coordinates = new int [2];
		view.getLocationOnScreen(coordinates);
		
		
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int pixels = (int)((50 * displayMetrics.density) + 0.5); //50 dp of margin - size of buttons
		
		height = display_height - (pixels + coordinates[1] + 3); //last magic number is margin value
	
		params.height = height;
		view.setLayoutParams(params);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.blind_control, menu);
	    
	    MenuItem menuSelectAll = menu.findItem(R.id.menu_select_all);
	    menuSelectAll.setVisible(delete_visibility);
	    		
	    MenuItem menuDeselectAll = menu.findItem(R.id.menu_deselect_all);
	    menuDeselectAll.setVisible(delete_visibility);
	    
	    if(delete_visibility){
		    if(delete_count == 0){
		    	menuSelectAll.setEnabled(true);
		    	menuDeselectAll.setEnabled(false);
		    }else if(delete_count == adapter.getCount()){
		    	menuSelectAll.setEnabled(false);
		    	menuDeselectAll.setEnabled(true);
		    }else{
		    	menuSelectAll.setEnabled(true);
		    	menuDeselectAll.setEnabled(true);
		    }
	    }
	    
	    menu.findItem(R.id.menu_add).setVisible(!delete_visibility);
	    menu.findItem(R.id.menu_ok).setVisible(!delete_visibility);
	    menu.findItem(R.id.menu_delete).setVisible(!delete_visibility);
	    
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		
		if(itemId == android.R.id.home){
				super.onBackPressed();
	    		return true;
		}else if(itemId == R.id.menu_ok){
			this.saveModifiedBlinds();
		}else if(itemId == R.id.menu_select_all){
			this.setSelectedRounds(true);
			adapter.notifyDataSetChanged();
			
		}else if(itemId == R.id.menu_deselect_all){
			this.setSelectedRounds(false);
			adapter.notifyDataSetChanged();
			
		}else if(itemId == R.id.menu_delete){
			setDeleteVisibility(true);
			this.showDeleteButtons();
	    	scaleDownListView();
			adapter.notifyDataSetChanged();
		// add new round
		}else if(itemId == R.id.menu_add){
			Intent intent = new Intent(this, EditRoundActivity.class);
			Round r = new Round();
			
			if(adapter.getCount() > 0){
				Round last_round = adapter.getItem(adapter.getCount()-1);
				r.setTime(last_round.getTime());
				r.setSB(last_round.getSB()*2);
				r.setBB(last_round.getBB()*2);
				r.setAnte(last_round.getAnte()*2);
				r.setNumberOfRound(last_round.getNumerOfRound() + 1);
			}else{
				r.setNumberOfRound(1);
			}
			
			r.setNew(true);
			
			intent.putExtra("Round", r);
			startActivityForResult(intent, ACTIVITY_EDIT_ROUND);
		}else{
		    return super.onOptionsItemSelected(item);
	    }
		
		return true;
	}
	
	/**
	 * Deletes checked rounds
	 */
	private void deleteCheckedRounds(){
		int number = 1;
		for(int i = 0; i < adapter.getCount(); i++){
			Round r = adapter.getItem(i);
			if(r.isForDelete() == true){
				adapter.remove(r);
				i--;
			}else{
				r.setNumberOfRound(number);
				number++;
			}
		}
	}
	
	/**
	 * Delete items from adapter
	 */
	public void delete(View view){
		this.deleteCheckedRounds();
		this.hideDeleteButtons();
		enlargeListView();
		adapter.notifyDataSetChanged();
		setDeleteVisibility(false);
		delete_count = 0;
	}
	
	/**
	 * Cancel deleting items
	 * @param v
	 */
	public void cancel(View view){
		setDeleteVisibility(false);
		enlargeListView();
		this.hideDeleteButtons();
		
		for(int i = 0; i < adapter.getCount(); i++){
			adapter.getItem(i).setForDelete(false);
		}
		
		adapter.notifyDataSetChanged();
		delete_count = 0;
	}
	
	private void setDeleteVisibility(boolean visibility){
		this.delete_visibility = visibility;
		invalidateOptionsMenu();
	}
	
	/**
	 * Send modified blinds back to the AddGameActivity
	 */
	private void saveModifiedBlinds(){
		// Game name validation
		if(textGameName.getText().toString().equals("")){
			textGameName.setError(getString(R.string.empty));
			textGameName.setOnClickListener(removeError);
			
		}else if(textGameName.getText().toString().length() > 25){
			textGameName.setError(getString(R.string.too_long) + 
			 " " + textGameName.getText().toString().length() + "." );
			textGameName.setOnClickListener(removeError);
			
		}else{
			// Number of rounds have to be greater than 1
			if(game.getRounds().size() == 0){
				new AlertDialog.Builder(this)
			    .setTitle(getString(R.string.warning))
			    .setMessage(getString(R.string.no_round_warning))
			    .setPositiveButton("OK", null)
			    .setCancelable(false)
			     .show();
			}else{
				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_OK, resultIntent);
				game.setName(textGameName.getText().toString());
				resultIntent.putExtra("Game", game);
				finish();
			}
		}
	}
	
	/**
	 * Selects/Unselects all rounds in list
	 */
	public void setSelectedRounds(boolean selected){
		for(Round r : this.rounds){
			r.setForDelete(selected);
		}
		
		if(selected){
			delete_count = this.rounds.size();
		}else{
			delete_count = 0;
		}
		
		invalidateOptionsMenu();
		refreshDeleteButton();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id){
		Intent intent = new Intent(this, EditRoundActivity.class);
		intent.putExtra("Round", rounds.get(position));
		startActivityForResult(intent, ACTIVITY_EDIT_ROUND);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case (ACTIVITY_EDIT_ROUND) : 
				if (resultCode == Activity.RESULT_OK) {
					Round newRound = (Round) data.getSerializableExtra("Round");
					if(newRound.isNew()){
						newRound.setNew(false);
						adapter.add(newRound);
					}else{
						Round r = adapter.getItem(newRound.getNumerOfRound() - 1);
						r.setTime(newRound.getTime());
						r.setSB(newRound.getSB());
						r.setBB(newRound.getBB());
						r.setAnte(newRound.getAnte());
						r.setBreak(newRound.isBreak());
					}
					
					adapter.notifyDataSetChanged();
				}
			break;
		}
	}
}