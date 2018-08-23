package com.capgemini.exceptions;

public class HighPrizeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7890003690582307903L;
	
	public HighPrizeException (){
		super("It's forbidden to buy over 5 items with unit prize over 7000 zl");
	}
	
	public HighPrizeException (Long no){
		super("It's forbidden to buy "+no.toString()+" items with unit prize over 7000 zl");
	}
	
}
