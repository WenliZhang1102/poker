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
		tlv.setRemoveListener(onRemove);
		tlv.setOnItemClickListener(this);
		
		
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
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
		
		textGameName.setText(this.game.getName()+"");
	}
	
	
	/*private class myOrientationEventListener extends OrientationEventListener{
		public myOrientationEventListener(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		/*myOrientationEventListener(){
			
		}

		@Override
		public void onOrientationChanged(int arg0) {
			// TODO Auto-generated method stub
			redrawRoundNumber();
		}
		
	}*/
	
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
	    
	    redrawRoundNumber();

	  }
	
	
	private void redrawRoundNumber(){
		ViewGroup round_list = (ViewGroup) findViewById(android.R.id.list);
		int childCount = round_list.getChildCount();

		for(int i = 0; i < childCount; i++){
			View view = round_list.getChildAt(i);
			TextView tv = (TextView) view.findViewById(R.id.round_number);
			tv.setText(String.valueOf(i+1)+".");
		}
	}
	
	private void showDeleteCheckbox(){
		
		//ViewGroup round_list = (ViewGroup) getLayoutInflater().inflate(android.R.id.list, null);
		ViewGroup round_list = (ViewGroup) findViewById(android.R.id.list);
		int childCount = round_list.getChildCount();

		for(int i = 0; i < childCount; i++){
			View view = round_list.getChildAt(i);
			view.findViewById(R.id.delete_checkbox).setVisibility(View.VISIBLE);
		}

			
		//findViewById(R.id.delete_checkbox).setVisibility(View.VISIBLE);
	}
	
	private void hideDeleteCheckbox(){
		
		ViewGroup round_list = (ViewGroup) findViewById(android.R.id.list);
		int childCount = round_list.getChildCount();

		for(int i = 0; i < childCount; i++){
			View view = round_list.getChildAt(i);
			view.findViewById(R.id.delete_checkbox).setVisibility(View.INVISIBLE);
		}
		
		//findViewById(R.id.delete_checkbox).setVisibility(View.INVISIBLE);
	}
	
	private void showDeleteButtons(){
		findViewById(R.id.horizontal_divider1).setVisibility(View.VISIBLE);
		findViewById(R.id.button_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.button_delete_checkbox).setVisibility(View.VISIBLE);
		findViewById(R.id.button_cancel_checkbox).setVisibility(View.VISIBLE);
		findViewById(R.id.vertical_divider).setVisibility(View.VISIBLE);
		findViewById(R.id.horizontal_divider2).setVisibility(View.VISIBLE);
	}
	
	private void hideDeleteButtons(){
		findViewById(R.id.horizontal_divider1).setVisibility(View.INVISIBLE);
		findViewById(R.id.button_layout).setVisibility(View.INVISIBLE);
		findViewById(R.id.button_delete_checkbox).setVisibility(View.INVISIBLE);
		findViewById(R.id.button_cancel_checkbox).setVisibility(View.INVISIBLE);
		findViewById(R.id.vertical_divider).setVisibility(View.INVISIBLE);
		findViewById(R.id.horizontal_divider2).setVisibility(View.INVISIBLE);
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
		/*this.stored_height = params.height;
		Toast.makeText(this, String.valueOf(params.height), Toast.LENGTH_SHORT).show();*/
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
	    redrawRoundNumber();//not for menu, but did not work in onCreate
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
			if(delete_visibility == true){
		    	this.hideDeleteCheckbox();
		    	this.hideDeleteButtons();
		    	enlargeListView();
		    	delete_visibility = false;
			}
			else{
		    	this.showDeleteCheckbox();
		    	this.showDeleteButtons();
		    	scaleDownListView();
		    	delete_visibility = true;
			}

	    	
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
	

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id){
		Intent intent = new Intent(this, EditRoundActivity.class);
		intent.putExtra("Round", rounds.get(position));
		startActivity(intent);
	}
}