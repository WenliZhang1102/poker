package com.example.pokertimer;

import java.io.Serializable;

public class Round implements Serializable {
	private static final long serialVersionUID = 2892861948011186607L;
	private long id;
	  private int number_of_round;
	  private int sb;
	  private int bb;
	  private int ante;
	  private int time;
	  
	  public Round(){}
	  
	  public Round(int number_of_round, int sb, int bb, int ante, int time){
		  this.number_of_round = number_of_round;
		  this.sb = sb;
		  this.bb = bb;
		  this.ante = ante;
		  this.time = time;
	  }

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return this.sb + "/" + this.bb + "/" + this.ante;
	  }
	  
	  public String toShortString(){
		  return this.sb + "/" + this.bb;
	  }

	/**
	 * @return the number_of_round
	 */
	public int getNumerOfRound() {
		return number_of_round;
	}

	/**
	 * @param number_of_round the number_of_round to set
	 */
	public void setNumberOfRound(int number_of_round) {
		this.number_of_round = number_of_round;
	}

	/**
	 * @return the seconds
	 */
	public int getSeconds() {
		return this.time % 60;
	}
	
	/**
	 * 
	 * @return minutes
	 */
	public int getMinutes(){
		return this.time / 60;
	}
	
	public int getTime(){
		return this.time;
	}

	/**
	 * @param seconds the seconds to set
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @return the sb
	 */
	public int getSB() {
		return sb;
	}

	/**
	 * @param sb the sb to set
	 */
	public void setSB(int sb) {
		this.sb = sb;
	}

	/**
	 * @return the bb
	 */
	public int getBB() {
		return bb;
	}

	/**
	 * @param bb the bb to set
	 */
	public void setBB(int bb) {
		this.bb = bb;
	}

	/**
	 * @return the ante
	 */
	public int getAnte() {
		return ante;
	}

	/**
	 * @param ante the ante to set
	 */
	public void setAnte(int ante) {
		this.ante = ante;
	}
} 