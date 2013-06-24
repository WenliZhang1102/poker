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
	private String[] allColumnsRound= { DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_SB, DatabaseHelper.COLUMN_BB, DatabaseHelper.COLUMN_ANTE, DatabaseHelper.COLUMN_SECONDS };

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
	 * Creates new game
	 * @param name Name of the game.
	 * @return Created game.
	 */
	public Game createGame(Game game) {
		// TODO: edit this bullshit
		this.open();
	    ContentValues values = new ContentValues();
	    values.put(DatabaseHelper.COLUMN_GAMENAME, game.getName());
	    long insertId = database.insert(DatabaseHelper.TABLE_GAMES, null, values); 
	    Cursor cursor = database.query(DatabaseHelper.TABLE_GAMES, allColumnsGame, DatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
	    cursor.moveToFirst();
	    Game newGame = cursorToGame(cursor);
	    cursor.close();
	    return newGame;
	}

	/**
	 * Delete game
	 * @param game
	 */
	public void deleteGame(Game game) {
		long id = game.getId();
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
	public List<Round> getAllRounds(Game game){
		List<Round> rounds = new ArrayList<Round>();

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
		round.setId(cursor.getLong(0));
		round.setSB(cursor.getInt(1));
		round.setBB(cursor.getInt(2));
		round.setAnte(cursor.getInt(3));
		round.setSeconds(cursor.getInt(4));
		return round;
	}
} 