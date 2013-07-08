package com.example.pokertimer;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumnsGame = { DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_GAMENAME };
	private String[] allColumnsRound= { DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_SB, DatabaseHelper.COLUMN_BB, DatabaseHelper.COLUMN_ANTE, DatabaseHelper.COLUMN_SECONDS, DatabaseHelper.COLUMN_BREAK, DatabaseHelper.COLUMN_NUMBER_OF_ROUND  };

	public DataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * Opens SQL for writing
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Closes db.
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Creates new game and saves blinds
	 * @param name Name of the game.
	 * @return Created game.
	 */
	public Game createGame(Game game) {
		this.open();
		
		// save game
	    ContentValues values = new ContentValues();
	    values.put(DatabaseHelper.COLUMN_GAMENAME, game.getName());
	    long insertId = database.insert(DatabaseHelper.TABLE_GAMES, null, values); 
	    Cursor cursor = database.query(DatabaseHelper.TABLE_GAMES, allColumnsGame, DatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
	    cursor.moveToFirst();
	    Game newGame = cursorToGame(cursor);
	    cursor.close();
	    
	    // save blinds
	    List<Round> rounds = game.getRounds();
	    for(Round round : rounds){
	    	values.clear();
	    	values.put(DatabaseHelper.COLUMN_SB, round.getSB());
	    	values.put(DatabaseHelper.COLUMN_BB, round.getBB());
	    	values.put(DatabaseHelper.COLUMN_ANTE, round.getAnte());
	    	values.put(DatabaseHelper.COLUMN_SECONDS, round.getTime());
	    	values.put(DatabaseHelper.COLUMN_NUMBER_OF_ROUND, round.getNumerOfRound());
	    	
	    	values.put(DatabaseHelper.COLUMN_ID_GAME, insertId);
	    	
	    	values.put(DatabaseHelper.COLUMN_BREAK, round.isBreak() ? 1 : 0);
	    	database.insert(DatabaseHelper.TABLE_ROUNDS, null, values);
	    }
	    
	    return newGame;
	}

	/**
	 * Delete game
	 * @param game
	 */
	public void deleteGame(Game game) {
		long id = game.getId();
		database.delete(DatabaseHelper.TABLE_ROUNDS, DatabaseHelper.COLUMN_ID_GAME + " = " + id, null);
		database.delete(DatabaseHelper.TABLE_GAMES, DatabaseHelper.COLUMN_ID + " = " + id, null);
	}

	/**
	 * Gets list of all games.
	 * @return list of games
	 */
	public List<Game> getAllGames() {
		List<Game> games = new ArrayList<Game>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_GAMES, allColumnsGame, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Game game = cursorToGame(cursor);
			games.add(game);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return games;
	}

	/**
	 * Gets all rounds for a game
	 * @param game 
	 * @return
	 */
	public ArrayList<Round> getAllRounds(Game game){
		ArrayList<Round> rounds = new ArrayList<Round>();

		Cursor cursor = database.query(DatabaseHelper.TABLE_ROUNDS, allColumnsRound, "_id_game = ?", new String[]{ Integer.toString((int) game.getId()) }, null, null, "number_of_round ASC");
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Round round = cursorToRound(cursor);
			rounds.add(round);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return rounds;
	}

	/**
	 * Gets cursor to games selection
	 * @param cursor
	 * @return
	 */
	private Game cursorToGame(Cursor cursor) {
		Game game = new Game();
		game.setId(cursor.getLong(0));
		game.setName(cursor.getString(1));
		return game;
	}
  
	/**
	 * Gets cursor to rounds selection
	 * @param cursor
	 * @return
	 */
	private Round cursorToRound(Cursor cursor) {
		Round round = new Round();
		round.setSB(cursor.getInt(1));
		round.setBB(cursor.getInt(2));
		round.setAnte(cursor.getInt(3));
		round.setTime(cursor.getInt(4));
		round.setBreak(cursor.getInt(5) == 1 ? true : false);
		round.setNumberOfRound(cursor.getInt(6));
		return round;
	}
} 