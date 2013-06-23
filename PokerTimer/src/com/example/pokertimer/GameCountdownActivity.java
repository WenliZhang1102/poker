package com.example.pokertimer;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameCountdownActivity extends Activity {
	
	private int roundNumber = 1;
	private Game game;
	
	private TextView textBlinds;
	private TextView textAnte;
	private TextView textNextBlinds;
	private TextView textTime;
	private List<Round> rounds;
	
	private boolean is_paused = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countdown);
		
		Intent countdownIntent = getIntent();
		this.game = (Game) countdownIntent.getSerializableExtra("Game");
		this.rounds = game.getRounds();
		
		
		textBlinds = (TextView) findViewById(R.id.blinds);
		textAnte = (TextView) findViewById(R.id.ante);
		textNextBlinds = (TextView) findViewById(R.id.next_blinds);
		textTime = (TextView) findViewById(R.id.time);
		
		// Set name of tournament
        setTitle(game.getName());
		
		refreshGameInfo();
		
		
		//Changing fonts for clock
		Typeface tf;
		//tf = Typeface.createFromAsset(getAssets(),"fonts/Swiss 721 Bold Rounded AT.TTF");
		tf = Typeface.createFromAsset(getAssets(),"fonts/UNVR47W.TTF");
		//tf = Typeface.createFromAsset(getAssets(),"fonts/BRLNSR.TTF");
		textTime.setTypeface(tf);
		
		ActionBar actionBar = getActionBar();
		//actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		

		
		/*
		new CountDownTimer(30000, 1000) {
	
		     public void onTick(long millisUntilFinished) {
				mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
		     }
	
		     public void onFinish() {
		         mTextField.setText("done!");
		     }
		  }.start();
		  */
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {       
        startActivity(new Intent(GameCountdownActivity.this,GamesActivity.class)); 
        return true;
    }
	
	/**
	 * Adds all texts to view from this round
	 */
	public void refreshGameInfo(){		
		Round round = rounds.get(roundNumber-1);
		
		if(roundNumber < rounds.size()){
			Round next_round = rounds.get(roundNumber);
			textNextBlinds.setText(next_round.toShortString());
		}else{
			textNextBlinds.setText("");
		}
		
		textBlinds.setText(round.toString());
		textAnte.setText(round.getAnte()+"");
	}
	
	
	public void onClick(View v) {
		View button = findViewById(R.id.button_play_pause);
		if(v == findViewById(R.id.button_play_pause)) {
			if(is_paused){
				
				button.setBackgroundResource(R.drawable.play);
				is_paused = false;
			}
			else{
				button.setBackgroundResource(R.drawable.pause);
				is_paused = true;
			}
			
		}
		
	
	}
}
