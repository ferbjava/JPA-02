package com.capgemini.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.capgemini.listeners.CreateListener;
import com.capgemini.listeners.UpdateListener;

@Entity
@EntityListeners({ CreateListener.class, UpdateListener.class })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "PRODUCT")
public class ProductEntity extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 45)
	private String productName;
	@Column(nullable = true)
	private Double unitPrice;
	@Column(nullable = false)
	private Integer margin;
	@Column(nullable = false)
	private Double weigth;

	// for hibernate
	public ProductEntity() {
	}

	public ProductEntity(Long id, String productName, Double unitPrice, Integer margin, Double weigth) {
		super(id);
		this.productName = productName;
		this.unitPrice = unitPrice;
		this.margin = margin;
		this.weigth = weigth;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getMargin() {
		return margin;
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
	}

	public Double getWeigth() {
		return weigth;
	}

	public void setWeigth(Double weigth) {
		this.weigth = weigth;
	}

}
