package com.example.pokertimer;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class GameCountdownActivity extends Activity {
	
	private int roundNumber = 1;
	private Game game;
	
	private TextView textBlinds;
	private TextView textAnte;
	private TextView textNextBlinds;
	private TextView textTime;
	private List<Round> rounds;
	
	private int time;
	
	private Round round;
	private Round next_round;
	
	private CountDownTimer countDownTimer = null;
	
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
		this.setRounds();
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
	}
	
	/**
	 * Starts next round
	 */
	public void setNextRound(){
		if(roundNumber < rounds.size()){
			roundNumber++;
			setRounds();
			stopCountDown();
		}
		
		refreshGameInfo();
	}
	
	public void startCountDown(){
		if(countDownTimer != null){
			stopCountDown();
		}
		
		countDownTimer = new CountDownTimer((this.time+1)*1000, 1000) {
		     public void onTick(long millisUntilFinished) {
		    	time = time-1;
				refreshTimeInfo();
		     }

			@Override
			public void onFinish() {
				setNextRound();
				startCountDown();
			}
		  }.start();
	}
	
	public void stopCountDown(){
		if(countDownTimer != null){
			countDownTimer.cancel();
		}
		
		countDownTimer = null;
	}
	
	/**
	 * Starts previous round
	 */
	public void setPreviousRound(){
		if(roundNumber - 1 > 0){
			roundNumber--;
			setRounds();
			stopCountDown();
		}
		
		refreshGameInfo();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {       
        startActivity(new Intent(GameCountdownActivity.this,GamesActivity.class)); 
        return true;
    }
	
	private void setRounds(){
		this.round = rounds.get(roundNumber-1);
		
		this.time = round.getSeconds();
		
		if(roundNumber < rounds.size()){
			this.next_round = rounds.get(roundNumber);
		}else{
			this.next_round = null;			
		}
	}
	
	/**
	 * Adds all texts to view from this round
	 */
	public void refreshGameInfo(){		
		
		if(next_round != null){
			textNextBlinds.setText(this.next_round.toString());
		}else{
			textNextBlinds.setText("");
		}
		
		textBlinds.setText(this.round.toShortString());
		textAnte.setText(this.round.getAnte()+"");
		refreshTimeInfo();
	}
	
	private void refreshTimeInfo(){
		int minutes = this.time / 60;
		int seconds = this.time % 60;
		
		
		
		textTime.setText(twoDigitString(minutes) + ":" + twoDigitString(seconds));
	}
	
	/**
	 * Creates two digits from one digit  (e. c. 9 -> "09"; 25 -> "25")
	 * @param number
	 * @return
	 */
	private String twoDigitString(Integer number){
		if(number < 10){
			return "0"+number;
		}else{
			return number.toString();
		}
	}
	
	public void onClick(View v) {
		View button = findViewById(R.id.button_play_pause);
		if(v == button) {
			if(is_paused){
				button.setBackgroundResource(R.drawable.play);
				is_paused = false;
				startCountDown();
			} else {
				button.setBackgroundResource(R.drawable.pause);
				is_paused = true;
				stopCountDown();
			}
		}
		
		if(v == findViewById(R.id.button_next)){
			setNextRound();
		}
		
		if(v == findViewById(R.id.button_previous)){
			setPreviousRound();
		}
	
	}
}
