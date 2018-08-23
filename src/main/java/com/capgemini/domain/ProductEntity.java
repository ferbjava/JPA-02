package com.capgemini.domain;

import java.io.Serializable;
import java.math.BigDecimal;

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
	private String name;
	@Column(nullable = true)
	private BigDecimal unitPrice;
	@Column(nullable = false)
	private Integer margin;
	@Column(nullable = false)
	private BigDecimal weigth;

	// for hibernate
	public ProductEntity() {
	}

	public ProductEntity(Long id, String name, BigDecimal unitPrice, Integer margin, BigDecimal weigth) {
		super(id);
		this.name = name;
		this.unitPrice = unitPrice;
		this.margin = margin;
		this.weigth = weigth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getMargin() {
		return margin;
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
	}

	public BigDecimal getWeigth() {
		return weigth;
	}

	public void setWeigth(BigDecimal weigth) {
		this.weigth = weigth;
	}

}
