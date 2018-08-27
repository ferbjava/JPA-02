package com.capgemini.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
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
	private BigDecimal price;
	@Column(nullable = false)
	private Integer margin;
	@Column(nullable = false)
	private BigDecimal weigth;
	@ManyToMany(mappedBy = "products")
	private
	List<TransactionEntity> transactions = new ArrayList<>();

	// for hibernate
	public ProductEntity() {
	}

	public ProductEntity(Long id, String name, BigDecimal price, Integer margin, BigDecimal weigth, List<TransactionEntity> transactions) {
		super(id);
		this.name = name;
		this.price = price;
		this.margin = margin;
		this.weigth = weigth;
		this.transactions = transactions;
	}

	public ProductEntity(Long id, String name, BigDecimal price, Integer margin, BigDecimal weigth) {
		super(id);
		this.name = name;
		this.price = price;
		this.margin = margin;
		this.weigth = weigth;
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

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public List<TransactionEntity> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionEntity> transactions) {
		this.transactions = transactions;
	}

	public void addTransaction(TransactionEntity transaction) {
		this.transactions.add(transaction);
	}

}
