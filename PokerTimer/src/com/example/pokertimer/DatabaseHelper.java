package com.example.pokertimer;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	protected Context context;
	public static final String DATABASE_NAME = "poker_blind_timer";
	
	public static final String TABLE_GAMES = "games";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_GAMENAME = "name";

	  private static final int DATABASE_VERSION = 1;
	
  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, 1);
    this.context = context;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    String s;
	try {
		InputStream in = context.getResources().openRawResource(R.raw.sql);
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(in, null);
		NodeList statements = doc.getElementsByTagName("statement");
		for (int i=0; i<statements.getLength(); i++) {
			s = statements.item(i).getChildNodes().item(0).getNodeValue();//err
			db.execSQL(s);
		}
	} catch (Throwable t) {
	}
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(DatabaseHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS games");
    onCreate(db);
  }

} 
