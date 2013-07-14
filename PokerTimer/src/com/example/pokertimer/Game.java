package com.example.pokertimer;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable{
	private static final long serialVersionUID = -6553576877034389434L;
	private long id = -1;
	private String name = "";
	private ArrayList<Round> rounds;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * @return the rounds
	 */
	public ArrayList<Round> getRounds() {
		return rounds;
	}

	/**
	 * @param rounds the rounds to set
	 */
	public void setRounds(ArrayList<Round> rounds) {
		this.rounds = rounds;
	}
} 