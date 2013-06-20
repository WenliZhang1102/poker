package com.example.pokertimer;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class AddGameActivity extends Activity {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        
       
        
        ActionBar ab = getActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayShowTitleEnabled(false);
    }

}
