package com.example.pokertimer;
import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;///asi zbytecny <--
import android.widget.ListView;
import android.widget.Toast;

public class GamesActivity extends ListActivity {
  private GamesDataSource datasource;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    datasource = new GamesDataSource(this);
    datasource.open();

    List<Game> values = datasource.getAllGames();

    // Use the SimpleCursorAdapter to show the
    // elements in a ListView
    ArrayAdapter<Game> adapter = new ArrayAdapter<Game>(this,
        android.R.layout.simple_list_item_1, values);
    setListAdapter(adapter);
    
    //my code
    
    ListView list = (ListView)findViewById(R.id.list);
    ///registerForContextMenu(list);                            <--------
	/*ListView lv = (ListView)findViewById(R.id.list); 
	lv.setClickable(false);
	lv.setOnItemClickListener(new OnItemClickListener()
	{
		 @Override
		 public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
	    { 
	        makeToast();
	    }
	});*/
	

	/*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
		   public void onItemClick(AdapterView parentView, View childView, int position, long id) {  
			   //makeToast();
		        }
		        public void onNothingClick(AdapterView parentView) {  

		        }  
		      });  
	
	*/
	
	//Toast.makeText(this, view.getId(), Toast.LENGTH_LONG).show();
  }
  
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    if (v.getId()==R.id.list) {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
      menu.setHeaderTitle("zblo");
      String[] menuItems =  new String[] { "mTitleRaw" };
      for (int i = 0; i<menuItems.length; i++) {
        menu.add(Menu.NONE, i, i, menuItems[i]);
      }
    }
  }
  
  //Shows top menu - Officially its called action bar
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.games, menu);
      return true;
  }
  
 /* @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {

      Log.i("Hello!", "Clicked! YAY!");

  }
  */
  protected void makeToast()
  {
  	Toast.makeText(this,
  			   "Úspìšnì jsi provedl/a realizaci kliknutí na tlaèítko.",
  			   Toast.LENGTH_SHORT).show();
  }
  
  protected String getName(){
  EditText Input = ((EditText)findViewById(R.id.editTextNew));
  String Str = Input.getText().toString();
 Input.setText("");
  return Str;
  }
  
 /* public void onHold(View view) {
	  
	  Toast.makeText(this, view.getId(), Toast.LENGTH_LONG).show();
	  
  }*/

  // Will be called via the onClick attribute
  // of the buttons in main.xml
  
  public void onClick(View view) {
    @SuppressWarnings("unchecked")
    ArrayAdapter<Game> adapter = (ArrayAdapter<Game>) getListAdapter();
    Game game = null;
    switch (view.getId()) {
    case R.id.add:
    	///zde se bude volat prepinani kontextu na nove okno nebo dialog, kde ziskame informace o vytvarene hre
      String newGameName = getName();//new String[] { "Cool", "Very nice", "Hate it" };
     if(newGameName.equals(""))
     {
    	 Toast.makeText(this, "Zadejte nazev noveho typu hry", Toast.LENGTH_LONG).show();
     }
     else
     {
      // Save the new game to the database
      game = datasource.createGame(newGameName);
      adapter.add(game);
     }
      break;
    case R.id.delete:
      if (getListAdapter().getCount() > 0) {
        game = (Game) getListAdapter().getItem(0);
        datasource.deleteGame(game);
        adapter.remove(game);
      }
      break;

    	
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  protected void onResume() {
    datasource.open();
    super.onResume();
  }

  @Override
  protected void onPause() {
    datasource.close();
    super.onPause();
  }

} 