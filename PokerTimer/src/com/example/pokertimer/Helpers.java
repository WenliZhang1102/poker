package com.example.pokertimer;

public class Helpers {
	/**
	 * Creates two digits from one digit  (e. c. 9 -> "09"; 25 -> "25")
	 * @param number
	 * @return
	 */
	public static String twoDigitString(Integer number){
		if(number < 10){
			return "0"+number;
		}else{
			return number.toString();
		}
	}
} 