package com.example.pokertimer;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.OrientationListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.tlv.TouchListView;

public class EditGameActivity extends ListActivity implements AdapterView.OnItemClickListener {
	
	private static final int ACTIVITY_EDIT_ROUND = 0;
	private Game game;
	private ArrayList<Round> rounds = null;
	
	private IconicAdapter adapter=null;
	
	private Boolean delete_visibility = false;

	EditText textGameName;
	
	private ArrayList<String> array;
	
	/**
	 * Set Activity content
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.blind_control);

        textGameName = (EditText) findViewById(R.id.game_name_edit);
        
        processIntent();
        
        TouchListView tlv=(TouchListView)getListView();
		adapter=new IconicAdapter();
		setListAdapter(adapter);
		
		tlv.setDropListener(onDrop);
		tlv.setOnItemClickListener(this);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setTitle("New game");
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
			
			// set round number
			TextView round_number = (TextView)row.findViewById(R.id.round_number);
			round_number.setText(rounds.get(position).getNumerOfRound() + ".");
			
			// set round info
			TextView label = (TextView)row.findViewById(R.id.label);
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
		
		textGameName.setText(this.game.getName()+"");
	}
	
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	    // Checks the orientation of the screen
	    
	    /*if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	        Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
	    }*/
	    
	    if(delete_visibility == true)
	    	scaleDownListView();
	  }
	
	/**
	 * Shows delete checkboxes
	 */
	private void showDeleteCheckbox(){
		ViewGroup round_list = (ViewGroup) findViewById(android.R.id.list);
		int childCount = round_list.getChildCount();

		for(int i = 0; i < childCount; i++){
			View view = round_list.getChildAt(i);
			view.findViewById(R.id.delete_checkbox).setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Hides delete checkboxes
	 * @param	boolean	Delete rounds with checked checkbox
	 */
	private void hideDeleteCheckbox(boolean delete){
		ViewGroup round_list = (ViewGroup) findViewById(android.R.id.list);
		round_list.invalidate();
		int childCount = round_list.getChildCount();

		int number = 0;
		for(int i = 0; i < childCount; i++){
			View view = round_list.getChildAt(i);
			CheckBox checkbox = (CheckBox) view.findViewById(R.id.delete_checkbox);
			
			if(checkbox.isChecked() && delete == true){
				adapter.remove(adapter.getItem(number));
			}else{
				adapter.getItem(number).setNumberOfRound(number + 1);
				number++;
			}
			
			checkbox.setVisibility(View.INVISIBLE);
	        checkbox.setChecked(false);
		}
	}
	
	private void showDeleteButtons(){
		findViewById(R.id.button_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.button_delete_checkbox).setVisibility(View.VISIBLE);
		findViewById(R.id.button_cancel_checkbox).setVisibility(View.VISIBLE);
	}
	
	private void hideDeleteButtons(){
		// hide buttons
		findViewById(R.id.button_layout).setVisibility(View.INVISIBLE);
		findViewById(R.id.button_delete_checkbox).setVisibility(View.INVISIBLE);
		findViewById(R.id.button_cancel_checkbox).setVisibility(View.INVISIBLE);
	}
	
	private void enlargeListView(){
		View view = findViewById(android.R.id.list);
		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height = LayoutParams.FILL_PARENT;
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
		    	
		}else if(itemId == R.id.menu_delete){
			if (delete_visibility == true) {
		    	this.hideDeleteCheckbox(false);
		    	this.hideDeleteButtons();
		    	enlargeListView();
		    	delete_visibility = false;
			} else {
				this.showDeleteCheckbox();
		    	this.showDeleteButtons();
		    	scaleDownListView();
		    	delete_visibility = true;
			}
			
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
	 * Delete items from adapter
	 */
	public void delete(View view){
		this.hideDeleteCheckbox(true);
		this.hideDeleteButtons();
		adapter.notifyDataSetChanged();
		delete_visibility = false;
	}
	
	/**
	 * Cancel deleting items
	 * @param v
	 */
	public void cancel(View view){
		this.hideDeleteCheckbox(false);
		this.hideDeleteButtons();
		delete_visibility = false;
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
					}
					
					adapter.notifyDataSetChanged();
				}
			break;
		}
	}
}