package com.capgemini.exceptions;

import java.math.BigDecimal;

public class TransactionHistoryException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8180211833225087518L;

	public TransactionHistoryException (){
		super("It's forbidden to perform purchase on over 5000 zl having less then 3 transactions");
	}

	public TransactionHistoryException (BigDecimal prize){
		super("It's forbidden to perform purchase on "+prize.toString()+"zl having less then 3 transactions");
	}
	
}
