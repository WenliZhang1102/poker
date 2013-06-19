package com.example.pokertimer;

public class Game {
	  private long id;
	  private String game;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getGame() {
	    return game;
	  }

	  public void setGame(String game) {
	    this.game = game;
	  }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return game;
	  }
	} 