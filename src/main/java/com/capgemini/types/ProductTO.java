package com.capgemini.types;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductTO {
	
	private Long id;
	private int version;
	private String name;
	private BigDecimal price;
	private Integer margin;
	private BigDecimal weigth;
	private List<Long> transactionsId = new ArrayList<>();
	
	public ProductTO() {
		super();
	}

	public ProductTO(Long id, int version, String name, BigDecimal price, Integer margin, BigDecimal weigth, List<Long> transactionsId) {
		super();
		this.id = id;
		this.version =version;
		this.name = name;
		this.price = price;
		this.margin = margin;
		this.weigth = weigth;
		this.transactionsId = transactionsId;
	}

	public Long getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Integer getMargin() {
		return margin;
	}

	public BigDecimal getWeigth() {
		return weigth;
	}

	public List<Long> getTransactionsId() {
		return transactionsId;
	}
	
	public ProductTOBuilder builder(){
		return new ProductTOBuilder();
	}
	
	public static class ProductTOBuilder{
		
		private Long id;
		private int version;
		private String name;
		private BigDecimal price;
		private Integer margin;
		private BigDecimal weigth;
		private List<Long> transactionsId = new ArrayList<>();
		
		public ProductTOBuilder() {
			super();
		}
		
		public ProductTOBuilder withId(Long id){
			this.id = id;
			return this;
		}
		
		public ProductTOBuilder withVersion(int version){
			this.version = version;
			return this;
		}
		
		public ProductTOBuilder withName(String name){
			this.name = name;
			return this;
		}
		
		public ProductTOBuilder withPrice(BigDecimal price){
			this.price = price;
			return this;
		}
		
		public ProductTOBuilder withMargin(Integer margin){
			this.margin = margin;
			return this;
		}
		
		public ProductTOBuilder withWeigth(BigDecimal weigth){
			this.weigth = weigth;
			return this;
		}
		
		public ProductTOBuilder withTransactiosId(List<Long> transactionsId){
			this.transactionsId = transactionsId;
			return this;
		}
		
		public ProductTO build(){
			checkBeforeBuild(name, price, margin, weigth);
			return new ProductTO(id, version, name, price, margin, weigth, transactionsId);
		}

		private void checkBeforeBuild(String name, BigDecimal price, Integer margin, BigDecimal weigth) {
			boolean isName = false;
			if (name != null && !name.isEmpty()) {
				isName = true;
			}
			boolean isPrice = false;
			if (price != null) {
				isPrice = true;
			}
			boolean isMargin = false;
			if (margin != null) {
				isMargin = true;
			}
			boolean isWeigth = false;
			if (weigth != null) {
				isWeigth = true;
			}
			if (!isName || !isPrice || !isMargin || !isWeigth) {
				throw new RuntimeException("Incorrect 'Product' to be created");
			}
		}
		
	}

}
