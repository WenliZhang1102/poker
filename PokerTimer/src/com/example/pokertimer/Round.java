package com.example.pokertimer;

import java.io.Serializable;

public class Round implements Serializable {
	  private long id;
	  private int number_of_round;
	  private int sb;
	  private int bb;
	  private int ante;
	  private int seconds;

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
		return seconds;
	}

	/**
	 * @param seconds the seconds to set
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
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