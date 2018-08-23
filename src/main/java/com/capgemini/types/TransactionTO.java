package com.capgemini.types;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionTO {
	
	private Long id;
	private int version;
	private Long clientId;
	private Calendar date;
	private String status;
	private List<Long> productsId = new ArrayList<>();
	
	public TransactionTO() {
		super();
	}

	public TransactionTO(Long id, int version, Long clientId, Calendar date, String status, List<Long> productsId) {
		super();
		this.id = id;
		this.version = version;
		this.clientId = clientId;
		this.date = date;
		this.status = status;
		this.productsId = productsId;
	}

	public Long getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	public Long getClientId() {
		return clientId;
	}

	public Calendar getDate() {
		return date;
	}

	public String getStatus() {
		return status;
	}

	public List<Long> getProductsId() {
		return productsId;
	}
	
	public TransactionTOBuilder builder(){
		return new TransactionTOBuilder();
	}

	public static class TransactionTOBuilder{
		
		private Long id;
		private int version;
		private Long clientId;
		private Calendar date;
		private String status;
		private List<Long> productsId = new ArrayList<>();
		
		public TransactionTOBuilder() {
			super();
		}
		
		public TransactionTOBuilder withId(Long id){
			this.id = id;
			return this;
		}
		
		public TransactionTOBuilder withVersion(int version){
			this.version = version;
			return this;
		}
		
		public TransactionTOBuilder withClientId(Long clientId){
			this.clientId = clientId;
			return this;
		}
		
		public TransactionTOBuilder withDate(Calendar date){
			this.date = date;
			return this;
		}
		
		public TransactionTOBuilder withStatus(String status){
			this.status = status;
			return this;
		}
		
		public TransactionTOBuilder withProductsIds(List<Long> productsId){
			this.productsId = productsId;
			return this;
		}
		
		public TransactionTO build(){
			System.out.println("Data: "+date.getTime());
			System.out.println("Status: "+status);
			checkBeforeBuild(date, status);
			return new TransactionTO(id, version, clientId, date, status, productsId);
		}

		private void checkBeforeBuild(Calendar date, String status) {
			if (date==null || status== null) {
				throw new RuntimeException("Incorrect 'Transaction' to be created");
			}
		}
	}
	
}
