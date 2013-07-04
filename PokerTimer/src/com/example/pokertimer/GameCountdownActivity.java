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
	
	private int time = -1; // stop timer
	
	private Round round;
	private Round next_round;
	
	private CountDownTimer countDownTimer = null;
	
	private boolean is_paused = true;
	
	View buttonPlayPause;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countdown);
		
		this.processIntent();
		
		textBlinds = (TextView) findViewById(R.id.blinds);
		textAnte = (TextView) findViewById(R.id.ante);
		textNextBlinds = (TextView) findViewById(R.id.next_blinds);
		textTime = (TextView) findViewById(R.id.time);
		buttonPlayPause = findViewById(R.id.button_play_pause);;
		
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
	 * Gets info from intent
	 */
	private void processIntent(){
		Intent countdownIntent = getIntent();
		this.game = (Game) countdownIntent.getSerializableExtra("Game");
		this.rounds = game.getRounds();
	}
	
	/**
	 * Starts Countdown
	 */
	public void startCountDown(){
		if(countDownTimer != null){
			stopCountDown();
		}
		
		if(time != -1){
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
			};
					  
			countDownTimer.start();
		}
	}
	
	/**
	 * Stops countDown
	 */
	public void stopCountDown(){
		if(countDownTimer != null){
			countDownTimer.cancel();
		}
		
		countDownTimer = null;
	}
	
	/**
	 * Starts next round
	 */
	public void setNextRound(){
		if(roundNumber < rounds.size()){
			roundNumber++;
			setRounds();
			stopCountDown();
		}else{
			if(countDownTimer != null){
				time = -1;
				setToStop();
			}
		}
		
		refreshGameInfo();
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
	
	/**
	 * Sets round and next_round
	 */
	private void setRounds(){
		this.round = rounds.get(roundNumber-1);
		
		this.time = round.getTime();
		
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
	
	/**
	 * Refresh time
	 */
	private void refreshTimeInfo(){
		if(this.time >= 0){
			int minutes = this.time / 60;
			int seconds = this.time % 60;
			textTime.setText(twoDigitString(minutes) + ":" + twoDigitString(seconds));
		}
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
		if(v == buttonPlayPause) {
			if(is_paused){
				setToPlay();
			} else {
				setToStop();
			}
		}else if(v == findViewById(R.id.button_next)){
			setNextRound();
			setToStop();
		}else if(v == findViewById(R.id.button_previous)){
			setPreviousRound();
			setToStop();
		}
	
	}
	
	private void setToPlay(){
		buttonPlayPause.setBackgroundResource(R.drawable.pause);
		is_paused = false;
		startCountDown();
	}
	
	private void setToStop(){
		buttonPlayPause.setBackgroundResource(R.drawable.play);
		is_paused = true;
		stopCountDown();
	}
}
