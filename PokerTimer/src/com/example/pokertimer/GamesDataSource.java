package com.example.pokertimer;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class GamesDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DatabaseHelper dbHelper;
  private String[] allColumns = { DatabaseHelper.COLUMN_ID,
      DatabaseHelper.COLUMN_GAMENAME };

  public GamesDataSource(Context context) {
    dbHelper = new DatabaseHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Game createGame(String game) {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.COLUMN_GAMENAME, game);
    long insertId = database.insert(DatabaseHelper.TABLE_GAMES, null,
        values);
    Cursor cursor = database.query(DatabaseHelper.TABLE_GAMES,
        allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Game newGame = cursorToGame(cursor);
    cursor.close();
    return newGame;
  }

  public void deleteGame(Game game) {
    long id = game.getId();
    database.delete(DatabaseHelper.TABLE_GAMES, DatabaseHelper.COLUMN_ID + " = " + id, null);
  }

  public List<Game> getAllGames() {
    List<Game> games = new ArrayList<Game>();

    Cursor cursor = database.query(DatabaseHelper.TABLE_GAMES,
        allColumns, null, null, null, null, null);

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

  private Game cursorToGame(Cursor cursor) {
    Game game = new Game();
    game.setId(cursor.getLong(0));
    game.setGame(cursor.getString(1));
    return game;
  }
} 