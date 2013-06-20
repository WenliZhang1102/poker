package com.example.pokertimer;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	protected Context context;
	public static final String DATABASE_NAME = "poker_blind_timer";
	
	public static final String TABLE_GAMES = "games";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_GAMENAME = "name";

	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	/**
	 * Creates new database + default data
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		executeSQL(db, R.raw.sql);
	}
	
	/**
	 * Upgrades database (if there is older version on the device)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		executeSQL(db, R.raw.sql_delete);
		onCreate(db);
	}

	/**
	 * Executes sql from some sql file
	 * @param db
	 * @param sql
	 */
	private void executeSQL(SQLiteDatabase db, int sql){
		String s;
		try {
			InputStream in = context.getResources().openRawResource(sql);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(in, null);
			NodeList statements = doc.getElementsByTagName("statement");
			for (int i=0; i<statements.getLength(); i++) {
			s = statements.item(i).getChildNodes().item(0).getNodeValue();//err
			db.execSQL(s);
		}
		} catch (Throwable t) {
			// TODO: catch
		}
	}
} 
