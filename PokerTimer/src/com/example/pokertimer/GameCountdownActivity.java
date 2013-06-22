package com.example.pokertimer;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class GameCountdownActivity extends Activity {
	
	private int roundNumber = 1;
	private Game game;
	private TextView textName;
	private TextView textBlinds;
	private TextView textAnte;
	private TextView textNextBlinds;
	private TextView textTime;
	private List<Round> rounds;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countdown);
		
		Intent countdownIntent = getIntent();
		this.game = (Game) countdownIntent.getSerializableExtra("Game");
		this.rounds = game.getRounds();
		
		textName = (TextView) findViewById(R.id.tournament_name);
		textBlinds = (TextView) findViewById(R.id.blinds);
		textAnte = (TextView) findViewById(R.id.ante);
		textNextBlinds = (TextView) findViewById(R.id.next_blinds);
		textTime = (TextView) findViewById(R.id.time);
		
		// Set name of tournament
		textName.setText(game.getName());
		
		refreshGameInfo();
		
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
}
