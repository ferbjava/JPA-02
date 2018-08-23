package com.capgemini.types;

import java.math.BigDecimal;

public class ProductTO {
	
	private Long id;
	private int version;
	private String name;
	private BigDecimal unitPrice;
	private Integer margin;
	private BigDecimal weigth;
	
	public ProductTO() {
		super();
	}

	public ProductTO(Long id, int version, String name, BigDecimal unitPrice, Integer margin, BigDecimal weigth) {
		super();
		this.id = id;
		this.version =version;
		this.name = name;
		this.unitPrice = unitPrice;
		this.margin = margin;
		this.weigth = weigth;
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

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public Integer getMargin() {
		return margin;
	}

	public BigDecimal getWeigth() {
		return weigth;
	}
	
	public ProductTOBuilder builder(){
		return new ProductTOBuilder();
	}
	
	public static class ProductTOBuilder{
		
		private Long id;
		private int version;
		private String name;
		private BigDecimal unitPrice;
		private Integer margin;
		private BigDecimal weigth;
		
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
		
		public ProductTOBuilder withUnitPrice(BigDecimal unitPrice){
			this.unitPrice = unitPrice;
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
		
		public ProductTO build(){
			checkBeforeBuild(name, unitPrice, margin, weigth);
			return new ProductTO(id, version, name, unitPrice, margin, weigth);
		}

		private void checkBeforeBuild(String name, BigDecimal unitPrice, Integer margin, BigDecimal weigth) {
			boolean isName = false;
			if (name != null && !name.isEmpty()) {
				isName = true;
			}
			boolean isUnitPrice = false;
			if (unitPrice != null) {
				isUnitPrice = true;
			}
			boolean isMargin = false;
			if (margin != null) {
				isMargin = true;
			}
			boolean isWeigth = false;
			if (weigth != null) {
				isWeigth = true;
			}
			if (!isName || !isUnitPrice || !isMargin || !isWeigth) {
				throw new RuntimeException("Incorrect 'Product' to be created");
			}
		}
		
	}

}
