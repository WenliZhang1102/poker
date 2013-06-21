package com.example.pokertimer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class GameCountdownActivity extends Activity {
	@Override
		public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countdown);
		
		final TextView mTextField = (TextView) findViewById(R.id.textView1);
		
		new CountDownTimer(30000, 1000) {

		     public void onTick(long millisUntilFinished) {
				mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
		     }

		     public void onFinish() {
		         mTextField.setText("done!");
		     }
		  }.start();
	}
}
