package com.capgemini.exceptions;

//public class TransactionHistoryException extends RuntimeException {
public class TransactionHistoryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8180211833225087518L;

	public TransactionHistoryException (){
		super("It's forbidden to perform purchase on over 5000 zl having less then 3 transactions");
	}
	
}
