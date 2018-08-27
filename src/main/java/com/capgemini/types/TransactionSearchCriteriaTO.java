package com.capgemini.types;

import java.math.BigDecimal;
import java.util.Calendar;

public class TransactionSearchCriteriaTO {

	private String lastName;
	private Calendar startDate;
	private Calendar endDate;
	private Long productId;
	private BigDecimal lowerCost;
	private BigDecimal upperCost;
	
	public TransactionSearchCriteriaTO(String lastName, Calendar startDate, Calendar endDate, Long productId,
			BigDecimal lowerCost, BigDecimal upperCost) {
		super();
		this.lastName = lastName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.productId = productId;
		this.lowerCost = lowerCost;
		this.upperCost = upperCost;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public BigDecimal getLowerCost() {
		return lowerCost;
	}

	public void setLowerCost(BigDecimal lowerCost) {
		this.lowerCost = lowerCost;
	}

	public BigDecimal getUpperCost() {
		return upperCost;
	}

	public void setUpperCost(BigDecimal upperCost) {
		this.upperCost = upperCost;
	}

}
