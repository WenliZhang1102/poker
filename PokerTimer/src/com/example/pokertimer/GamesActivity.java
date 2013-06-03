package com.example.pokertimer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class GamesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        Toast.makeText(this, "Komoi tì zdraví!", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.games, menu);
        return true;
    }
    
    public void buttonClicked(View view)
    {
    	EditText et=(EditText)findViewById(R.id.edittext);
    	String txt=et.getText().toString();
    	 Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
    	
    }
}
