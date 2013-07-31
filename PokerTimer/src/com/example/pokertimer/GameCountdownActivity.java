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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class GameCountdownActivity extends Activity {
	
	private int roundNumber = 1;
	private Game game;
	
	private TextView textBlinds;
	private TextView textAnte;
	private TextView textNextBlinds;
	private TextView textTime;
	
	private TextView textBreak;
	private TextView textNumberOfRound;
	
	private List<Round> rounds;
	
	private RelativeLayout blinds_layout;
	private RelativeLayout ante_layout;
	
	private int time = -1; // stop timer
	
	private Round round;
	private Round next_round;
	
	private CountDownTimer countDownTimer = null;
	
	private boolean is_paused = true;
	private boolean can_leave = false;
	
	private SharedPreferences sharedPref;
	
	View buttonPlayPause;
	
	Intent countdownIntent;
	
	private Animation mInFromRight;
    private Animation mOutToLeft;
    private Animation mInFromLeft;
    private Animation mOutToRight;
    private ViewFlipper mViewFlipper;
    
    private final int SWIPE_SPEED = 300;
    
    private int defTimeout;
    
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    
    private int active_layout = R.id.layout1;
	
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
		
		buttonPlayPause = findViewById(R.id.button_play_pause);
		
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
		View layout1 = findViewById(R.id.layout1);
		View layout2 = findViewById(R.id.layout2);
		((TextView)layout1.findViewById(R.id.time)).setTypeface(tf);
		((TextView)layout2.findViewById(R.id.time)).setTypeface(tf);
		
		ActionBar actionBar = getActionBar();
		//actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		setNotification();
		
		if(sharedPref.getBoolean(SettingsActivity.KEY_USE_LOCKSCREEN, true)){
    		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		}
	}
	
	private void toggleLayout(){
	    this.active_layout = secondLayout();
	}
	
	private int secondLayout(){
	    if(this.active_layout == R.id.layout1){
            return R.id.layout2;
        }else{
            return R.id.layout1;
        }
	    
	}
	
	private void findViews(){
	    
	    View layout = findViewById(this.active_layout);
	    
	    textBlinds = (TextView) layout.findViewById(R.id.blinds);
        textAnte = (TextView) layout.findViewById(R.id.ante);
        textNextBlinds = (TextView) layout.findViewById(R.id.next_blinds);
        textTime = (TextView) layout.findViewById(R.id.time);
        
        textBreak = (TextView) layout.findViewById(R.id.break_tv);
        textNumberOfRound = (TextView) layout.findViewById(R.id.number_of_round);

	}
	
	@Override
	public void onBackPressed() {
		if(is_paused == true)
			super.onBackPressed();
		else
		{
			if(can_leave == true)
			{
				super.onBackPressed();
			}
			else
			{
				Toast.makeText(this, "Press back again to leave", Toast.LENGTH_LONG).show();
				can_leave = true;
				Thread thread=  new Thread(){
			        @Override
			        public void run(){
			            try {
			                synchronized(this){
			                    wait(3000);
			                    can_leave = false;
			                    
			                }
			            }
			            catch(InterruptedException ex){                    
			            }

			            // TODO              
			        }
			    };

			    thread.start();   
			}
			
			
		}
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
	        mInFromRight.setDuration(SWIPE_SPEED);
	        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
	        mInFromRight.setInterpolator(accelerateInterpolator);

	        mInFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
	                -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        mInFromLeft.setDuration(SWIPE_SPEED);
	        mInFromLeft.setInterpolator(accelerateInterpolator);

	        mOutToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
	                0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        mOutToRight.setDuration(SWIPE_SPEED);
	        mOutToRight.setInterpolator(accelerateInterpolator);

	        mOutToLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, -1.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        mOutToLeft.setDuration(SWIPE_SPEED);
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

	        private static final int SWIPE_MIN_DISTANCE = 90;
	        private static final int SWIPE_MAX_OFF_PATH = 250;
	        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	                return false;
	            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
	                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                mViewFlipper.setInAnimation(mInFromRight);
	                mViewFlipper.setOutAnimation(mOutToLeft);
	                
	                if(!isLastRound()){
	                    toggleLayout();
	                    findViews();
	                    setToStop();
	                    setNextRound();
	                	mViewFlipper.showNext();
	                }
	    			
	            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
	                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                mViewFlipper.setInAnimation(mInFromLeft);
	                mViewFlipper.setOutAnimation(mOutToRight);
	                
	                if(!isFirstRound()){
	                    toggleLayout();
	                    findViews();
	                    setPreviousRound();
	                    setToStop();
                        mViewFlipper.showPrevious();
	                }
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
                .setSmallIcon(R.drawable.ic_launcher);


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
			text = "Blinds: " + this.next_round.getSB() + "/" + this.next_round.getBB() + 
					",  Ante: " +  this.next_round.getAnte();
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
	 * Is current level last round?
	 * @return
	 */
	public boolean isLastRound(){
	    return roundNumber + 1 == rounds.size();
	}
	
	/**
     * Is current level first round?
     * @return
     */
	public boolean isFirstRound(){
	    return roundNumber == 1;
	}
	
	/**
	 * Starts next round
	 */
	public boolean setNextRound(){
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
	public boolean setPreviousRound(){
		if(roundNumber > 1){
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
			
			//if(is_paused == true)
			super.onBackPressed();
			//moveTaskToBack(true);	
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
			textNumberOfRound.setText(this.roundNumber+". round");
			
		}else{
			textNextBlinds.setText("");
		}
		
		if(this.round.isBreak()){
			findViewById(R.id.this_round_blinds).setVisibility(View.GONE);
			findViewById(R.id.this_round_ante).setVisibility(View.GONE);
			textAnte.setVisibility(View.GONE);
			textBlinds.setVisibility(View.GONE);
			
			//if(textBreak != null)
				textBreak.setVisibility(View.VISIBLE);
			
			
		}else{
			findViewById(R.id.this_round_blinds).setVisibility(View.VISIBLE);
			findViewById(R.id.this_round_ante).setVisibility(View.VISIBLE);
			textAnte.setVisibility(View.VISIBLE);
			textBlinds.setVisibility(View.VISIBLE);
			
			if(textBreak != null)
				textBreak.setVisibility(View.GONE);
			
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
