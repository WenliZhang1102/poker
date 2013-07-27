package com.example.pokertimer;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.provider.Settings;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class GameCountdownActivity extends Activity {
	
	private int roundNumber = 1;
	private Game game;
	
	private TextView textBlinds;
	private TextView textAnte;
	private TextView textNextBlinds;
	private TextView textTime;
	private List<Round> rounds;
	
	private RelativeLayout blinds_layout;
	private RelativeLayout ante_layout;
	
	private int time = -1; // stop timer
	
	private Round round;
	private Round next_round;
	
	private CountDownTimer countDownTimer = null;
	
	private boolean is_paused = true;
	
	private SharedPreferences sharedPref;
	
	View buttonPlayPause;
	
	Intent countdownIntent;
	
	private Animation mInFromRight;
    private Animation mOutToLeft;
    private Animation mInFromLeft;
    private Animation mOutToRight;
    private ViewFlipper mViewFlipper;
    
    private int defTimeout;
    
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countdown);
		
		 mViewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
	        mViewFlipper.setDisplayedChild(0);
	        mViewFlipper.setAnimateFirstView(true);
	        initAnimations();
		
		
		this.processIntent();
		
		findViews();
		
		//Landscape transformation inicialization
		//SetLayoutParams();
		
		//if is landscape now, set this layout transformation. If not, let it be (portrait is default)
		/*if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			setLandscape();*/
		//else
			//setPortrait();
		
		// Set name of tournament
        setTitle(game.getName());
		this.setRounds();
		refreshGameInfo();
		
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		defTimeout = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 6000);
		
		if(sharedPref.getBoolean(SettingsActivity.KEY_NEVER_OFF_SCREEN, false)){
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, -1);
        }else{
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, defTimeout);
        }
		
		//Changing fonts for clock
		Typeface tf;
		//tf = Typeface.createFromAsset(getAssets(),"fonts/Swiss 721 Bold Rounded AT.TTF");
		tf = Typeface.createFromAsset(getAssets(),"fonts/UNVR47W.TTF");
		//tf = Typeface.createFromAsset(getAssets(),"fonts/BRLNSR.TTF");
		textTime.setTypeface(tf);
		
		ActionBar actionBar = getActionBar();
		//actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		setNotification();
	}
	
	private void findViews(){
	    textBlinds = (TextView) findViewById(R.id.blinds);
        textAnte = (TextView) findViewById(R.id.ante);
        textNextBlinds = (TextView) findViewById(R.id.next_blinds);
        textTime = (TextView) findViewById(R.id.time);
        buttonPlayPause = findViewById(R.id.button_play_pause);
        
       /* blinds_layout = (RelativeLayout) findViewById(R.id.blinds_layout);
        ante_layout = (RelativeLayout) findViewById(R.id.ante_layout);*/
	}
	
	@Override
	public void onBackPressed() {
		if(is_paused == true)
			super.onBackPressed();
		else
			moveTaskToBack(true);
	}
	
	/*private void setLandscape(){
		RelativeLayout.LayoutParams blinds_params = (RelativeLayout.LayoutParams)blinds_layout.getLayoutParams();
		//landscape transformations
    	blinds_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    	blinds_params.addRule(RelativeLayout.LEFT_OF, R.id.strut);
    	blinds_params.addRule(RelativeLayout.BELOW, R.id.time);
    	
    	RelativeLayout.LayoutParams ante_params = (RelativeLayout.LayoutParams)ante_layout.getLayoutParams();
    	//landscape transformations
    	ante_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	ante_params.addRule(RelativeLayout.RIGHT_OF, R.id.strut);
    	ante_params.addRule(RelativeLayout.BELOW, R.id.time);
    	
		
    	this.blinds_layout.setLayoutParams(blinds_params);
    	this.ante_layout.setLayoutParams(ante_params);
		
	}
	
	private void setPortrait(){
		
		RelativeLayout.LayoutParams blinds_params = (RelativeLayout.LayoutParams)blinds_layout.getLayoutParams();
		//portrait transformations
    	blinds_params.addRule(RelativeLayout.LEFT_OF, 0);
    	blinds_params.addRule(RelativeLayout.BELOW, R.id.time);
    	
    	RelativeLayout.LayoutParams ante_params = (RelativeLayout.LayoutParams)ante_layout.getLayoutParams();
    	//portrait transformations
    	ante_params.addRule(RelativeLayout.RIGHT_OF, 0);
    	ante_params.addRule(RelativeLayout.BELOW, R.id.blinds_layout);

    	this.blinds_layout.setLayoutParams(blinds_params);
    	this.ante_layout.setLayoutParams(ante_params);
		
	}
	
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		 if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			 setLandscape();
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	    	setPortrait();
	    }
	  }
	*/
	
	
	 private void initAnimations() {
	        mInFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
	                +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        mInFromRight.setDuration(500);
	        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
	        mInFromRight.setInterpolator(accelerateInterpolator);

	        mInFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
	                -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        mInFromLeft.setDuration(500);
	        mInFromLeft.setInterpolator(accelerateInterpolator);

	        mOutToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
	                0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        mOutToRight.setDuration(500);
	        mOutToRight.setInterpolator(accelerateInterpolator);

	        mOutToLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, -1.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        mOutToLeft.setDuration(500);
	        mOutToLeft.setInterpolator(accelerateInterpolator);

	        final GestureDetector gestureDetector;
	        gestureDetector = new GestureDetector(new MyGestureDetector());

	        mViewFlipper.setOnTouchListener(new OnTouchListener() {

	            public boolean onTouch(View v, MotionEvent event) {
	                if (gestureDetector.onTouchEvent(event)) {
	                    return false;
	                } else {
	                    return true;
	                }
	            }
	        });
	    }

	    private class MyGestureDetector extends SimpleOnGestureListener {

	        private static final int SWIPE_MIN_DISTANCE = 120;
	        private static final int SWIPE_MAX_OFF_PATH = 250;
	        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	                float velocityY) {
	            System.out.println(" in onFling() :: ");
	            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	                return false;
	            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
	                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                mViewFlipper.setInAnimation(mInFromRight);
	                mViewFlipper.setOutAnimation(mOutToLeft);
	                
	                if(setNextRound() == true)
	                	mViewFlipper.showNext();
	                
	    			setToStop();
	    			
	            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
	                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                mViewFlipper.setInAnimation(mInFromLeft);
	                mViewFlipper.setOutAnimation(mOutToRight);
	                
	                if(setPreviousRound() == true)
	                	mViewFlipper.showPrevious();
	                
	    			setToStop();
	            }
	            return super.onFling(e1, e2, velocityX, velocityY);
	        }
	    }
	
	/**
	 * Gets info from intent
	 */
	private void processIntent(){
		countdownIntent = getIntent();
		this.game = (Game) countdownIntent.getSerializableExtra("Game");
		this.rounds = game.getRounds();
	}
	
	private void setNotification(){
	    notificationBuilder =  
                new NotificationCompat.Builder(this)  
                .setSmallIcon(R.drawable.forward);


        Intent notificationIntent = countdownIntent;// new Intent(this, GameCountdownActivity.class);  
        
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,   
                PendingIntent.FLAG_UPDATE_CURRENT);  

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);
        
        notificationBuilder.setContentIntent(contentIntent);  
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setLights(Color.GREEN, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        
        if(sharedPref.getBoolean(SettingsActivity.KEY_VIBRATIONS, false)){
            notificationBuilder.setVibrate(pattern);
        }
        
        notificationBuilder.setStyle(new NotificationCompat.InboxStyle());
        // Add as notification  
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	private void notifyNewRound(){
		String text = new String();
		String title = new String();
		
		if(next_round != null){
			title = this.next_round.getNumerOfRound() + ". " + getText(R.string.round);
			text = "Blinds:  " + this.next_round.toString() + ",  Duration:  " 
			        + String.format("%02d", next_round.getMinutes()) +":"
			        + String.format("%02d", next_round.getSeconds());
		} else {
			title = "Game finished!";
			text = "";
		}
		
		if(sharedPref.getBoolean(SettingsActivity.KEY_NOTIFICATIONS, false)){
    		notificationBuilder.setContentTitle(title).setContentText(text);  
            notificationManager.notify(1, notificationBuilder.build());
		}
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
				
				@Override
				public void onTick(long millisUntilFinished) {
					time = time-1;
					if(time >= 0){
						refreshTimeInfo();
					}else{
						notifyNewRound();
						setNextRound();
					}
				}
				
				@Override
				public void onFinish() {
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
	public Boolean setNextRound(){
		if(roundNumber < rounds.size()){
			roundNumber++;
			setRounds();
			stopCountDown();
		}else if(countDownTimer != null){
				time = -1;
				setToStop();
		}
		else{
			return false;
		}

		
		refreshGameInfo();
		return true;
	}
	
	/**
	 * Starts previous round
	 */
	public Boolean setPreviousRound(){
		if(roundNumber - 1 > 0){
			roundNumber--;
			setRounds();
			stopCountDown();
		}
		else{
			return false;
		}
		
		refreshGameInfo();
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
		int itemId = menuItem.getItemId();
		
		if(itemId == android.R.id.home){
			
			if(is_paused == true)
				super.onBackPressed();
			else
				moveTaskToBack(true);	
		}
		
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
		
		if(this.round.isBreak()){
			findViewById(R.id.this_round_blinds).setVisibility(View.GONE);
			findViewById(R.id.this_round_ante).setVisibility(View.GONE);
			textAnte.setVisibility(View.GONE);
			
			textBlinds.setTextSize(70);
			textBlinds.setGravity(Gravity.CENTER_VERTICAL);
			
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)textBlinds.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

			textBlinds.setLayoutParams(params);
			
		}else{
			findViewById(R.id.this_round_blinds).setVisibility(View.VISIBLE);
			findViewById(R.id.this_round_ante).setVisibility(View.VISIBLE);
			textAnte.setVisibility(View.VISIBLE);
			
			textBlinds.setTextSize(40);
			textBlinds.setGravity(Gravity.RIGHT);
			
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)textBlinds.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			textBlinds.setLayoutParams(params);
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
			textTime.setText(Helpers.twoDigitString(minutes) + ":" + Helpers.twoDigitString(seconds));
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
		buttonPlayPause.setBackgroundResource(R.drawable.pause_button);
		is_paused = false;
		startCountDown();
	}
	
	private void setToStop(){
		buttonPlayPause.setBackgroundResource(R.drawable.play_button);
		is_paused = true;
		stopCountDown();
	}
	
	@Override
    protected void onDestroy() 
    {
        super.onDestroy();
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, defTimeout);
    }
}
