package com.example.pokertimer;

import java.io.Serializable;

public class Round implements Serializable {
	private static final long serialVersionUID = 2892861948011186607L;
	private int number_of_round;
	private int sb;
	private int bb;
	private int ante;
	private int time;
	private boolean is_new = false;
	private boolean is_break = false;
	private boolean for_delete = false; // for listView 
	  
	public Round(){}
	  
	public Round(int number_of_round, int sb, int bb, int ante, int time){
		this.number_of_round = number_of_round;
		this.sb = sb;
		this.bb = bb;
		this.ante = ante;
		this.time = time;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
	    if(this.is_break == true){
    	    return "BREAK";
	    }else{
	        return this.sb + "/" + this.bb + "/" + this.ante;
	    }
	}
	  
	public String toShortString(){
	    if(this.is_break == true){
            return "BREAK";
        }else{
            return this.sb + "/" + this.bb;
        }
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
	
	public String getReadableTime(){
		int minutes = getMinutes();
		int seconds = getSeconds();
		
		return Helpers.twoDigitString(minutes) + ":" + Helpers.twoDigitString(seconds);
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
	 * @param minutes
	 * @param seconds
	 */
	public void setTime(int minutes, int seconds){
	    this.time = minutes*60 + seconds;
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
	
	/**
	 * @param is_break is this round a break?
	 */
	public void setBreak(boolean is_break){
		this.is_break = is_break;
		if(is_break == true){
			setSB(0);
			setBB(0);
			setAnte(0);
		}
	}
	
	/**
	 * Is this round a break?
	 * @return
	 */
	public boolean isBreak(){
		return is_break;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isNew(){
		return this.is_new;
	}
	/**
	 * 
	 * @param is_new
	 */
	public void setNew(boolean is_new){
		this.is_new = is_new;
	}

	/**
	 * @return the for_delete
	 */
	public boolean isForDelete() {
		return for_delete;
	}

	/**
	 * @param for_delete the for_delete to set
	 */
	public void setForDelete(boolean for_delete) {
		this.for_delete = for_delete;
	}
}